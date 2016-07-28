package com.qfg.ctu.servlet.rest.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.ProductDao;
import com.qfg.ctu.dao.pojo.Product;
import com.qfg.ctu.servlet.rest.exception.InvalidRequestException;
import com.qfg.ctu.servlet.rest.pojos.RestProduct;
import org.jvnet.hk2.annotations.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 7/26/16.
 */

@Service
public class ProductService {
    private static String TBH = "http://www.tbh.cn/member_api/product.php";

    public List<RestProduct> getAll() {
        ProductDao productDao = DaoFactory.getProductDao(null);
        List<Product> products = productDao.findAll();
        List restProducts = products.stream().map(n->new RestProduct(n)).collect(Collectors.toList());
        return restProducts;
    }

    public RestProduct getByBarcode(int id) throws Exception {
        ProductDao productDao = DaoFactory.getProductDao(null);
        return new RestProduct(productDao.findById(id));
    }

    public RestProduct getByQR(String id) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader in = null;
        PrintWriter printWriter = null;
        try{
            URL url=new URL(TBH);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            printWriter = new PrintWriter(conn.getOutputStream());
            printWriter.write(String.format("act=get_product_by_unique_code&unique_code=%s", id));
            printWriter.flush();

            in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8") );
            JsonObject jsonObject = (new JsonParser()).parse(in).getAsJsonObject();
            JsonObject info = jsonObject.getAsJsonObject("info");

            return getByBarcode(info.get("product_code").getAsInt());
        } catch (Exception ex) {
            throw new InvalidRequestException(ex.getMessage());
        } finally{
            try{
                if (null != conn) conn.disconnect();
                if(in!=null){
                    in.close();
                }
                if(printWriter!=null){
                    printWriter.close();
                }
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
