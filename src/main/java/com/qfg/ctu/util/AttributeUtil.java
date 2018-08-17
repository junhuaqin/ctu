package com.santaba.server.servlet.rest.utils;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueNullableAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.codegen.AttributeBytecodeGenerator;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.santaba.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.googlecode.cqengine.stream.StreamFactory.streamOf;

/**
 * Created by Robert Qin on 26/04/2018.
 */
public class AttributeUtil {
    private static Map<Class<?>, Map<String, ? extends Attribute<?, ?>>> _attributes = new ConcurrentHashMap<>();
    private static Reflections _reflections = new Reflections(
            new ConfigurationBuilder().forPackages(
                    "com.santaba.server.servlet.rest.base.pojos",
                    "com.santaba.server.servlet.rest.v1.pojos",
                    "com.santaba.server.servlet.rest.v2.pojos")
    );

    public static <O> boolean hasAttribute(Class<O> pojoClass, String attributeName, boolean includeSubClass) {
        try {
            return Objects.nonNull(_get(pojoClass, attributeName, includeSubClass));
        }
        catch (Exception e) {
            return false;
        }
    }

    static <O> Attribute<O, ?> get(Class<O> pojoClass, String attributeName) {
        return _get(pojoClass, attributeName, true);
    }

    private static <O> Attribute<O, ?> _get(Class<O> pojoClass, String attributeName, boolean includeSubClass) {
        if (includeSubClass) {
            Set<Class<? extends O>> subTypes = _reflections.getSubTypesOf(pojoClass);
            if (CollectionUtils.isEmpty(subTypes)) {
                return _getSingle(pojoClass, attributeName);
            }
            else {
                return _getIncludeSubClass(pojoClass, subTypes, attributeName);
            }
        }
        else {
            return _getSingle(pojoClass, attributeName);
        }
    }

    private static <O> void _register(Class<O> pojoClass) {
        _attributes.computeIfAbsent(pojoClass, AttributeUtil::_initAttributes);
    }

    private static <O> Map<String, ? extends Attribute<O, ?>> _initAttributes(Class<O> pojoClass) {
        return AttributeBytecodeGenerator.createAttributes(pojoClass);
    }

    @SuppressWarnings("unchecked")
    private static <O> Attribute<O, ?> _getFlat(Class<O> pojoClass, String attributeName) {
        _register(pojoClass);
        return (Attribute<O, ?>)
                Optional.ofNullable(_attributes.get(pojoClass))
                        .map(m -> m.get(attributeName))
                        .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static <O> Collection<Attribute<O, ?>> _getAll(Class<O> pojoClass) {
        _register(pojoClass);
        return Optional.ofNullable(_attributes.get(pojoClass))
                .map(Map::values)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(attr -> (Attribute<O, ?>)attr)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <O> Attribute<O, ?> _getSingle(Class<O> pojoClass, String attributeName) {
        if (FieldFilter.FIELD_ALL.equals(attributeName)) {
            Collection<Attribute<O, ?>> attributes = _getAll(pojoClass);
            return new SimpleAttribute<O, String>(pojoClass, String.class, attributeName) {
                @Override
                public String getValue(O object, QueryOptions queryOptions) {
                    return attributes.stream()
                            .map(attr -> attr.getValues(object, queryOptions).toString())
                            .collect(Collectors.joining(" "));
                }
            };
        }

        if (attributeName.indexOf('.') <= 0) {
            return _getFlat(pojoClass, attributeName);
        }

        String fields[] = StringUtil.split(attributeName, ".", true);
        String restFields[] = Arrays.copyOfRange(fields, 1, fields.length);
        String rest = Stream.of(restFields).collect(Collectors.joining("."));

        Attribute<O, ?> attribute = _getFlat(pojoClass, fields[0]);
        if (Objects.isNull(attribute)) {
            return null;
        }

        Attribute<Object, Object> restAttribute = (Attribute<Object, Object>)_getSingle(attribute.getAttributeType(), rest);
        if (Objects.isNull(restAttribute)) {
            return null;
        }

        return new MultiValueNullableAttribute<O, Object>(pojoClass, restAttribute.getAttributeType(), attributeName, true) {
            @Override
            public Iterable<Object> getNullableValues(O object, QueryOptions queryOptions) {
                return streamOf(attribute.getValues(object, queryOptions))
                        .flatMap(n -> streamOf(restAttribute.getValues(n, queryOptions)))
                        .collect(Collectors.toList());
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <O> Attribute<O, ?> _getIncludeSubClass(Class<O> superClz, Set<Class<? extends O>> pojoClasses, String attributeName) {
        Set<Class<? extends O>> classes = new HashSet<>(pojoClasses);
        classes.add(superClz);

        Map<Class<?>, Attribute> attributes = new HashMap<>();
        Class attributeType = null;
        boolean isSimple = true;

        for (Class<? extends O> clz : classes) {
            Attribute attribute = _getSingle(clz, attributeName);
            if (Objects.nonNull(attribute)) {
                boolean isCurSimple = attribute instanceof SimpleAttribute || attribute instanceof SimpleNullableAttribute;
                if (Objects.isNull(attributeType)) {
                    attributeType = attribute.getAttributeType();
                    isSimple = isCurSimple;
                }
                else if (isSimple != isCurSimple || !attributeType.equals(attribute.getAttributeType())) {
                    return null;
                }
                attributes.put(clz, attribute);
            }
        }

        if (attributes.isEmpty()) {
            return null;
        }

        if (isSimple) {
            return new SimpleNullableAttribute<O, Object>(superClz, attributeType, attributeName) {
                @Override
                public Object getValue(O object, QueryOptions queryOptions) {
                    return attributes.entrySet()
                            .stream()
                            .filter(e -> e.getKey().isAssignableFrom(object.getClass()))
                            .findFirst()
                            .flatMap(e -> streamOf(e.getValue().getValues(object, queryOptions)).findFirst())
                            .orElse(null);
                }
            };
        }

        return new MultiValueNullableAttribute<O, Object>(superClz, attributeType, attributeName, true) {
            @Override
            public Iterable<Object> getNullableValues(O object, QueryOptions queryOptions) {
                return attributes.entrySet()
                        .stream()
                        .filter(e -> e.getKey().isAssignableFrom(object.getClass()))
                        .findFirst()
                        .map(e -> e.getValue().getValues(object, queryOptions))
                        .orElse(null);
            }
        };
    }
}

