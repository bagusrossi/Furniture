package com.example.furniture.Helpers;

import android.util.Log;

import com.example.furniture.Models.Categories;
import com.example.furniture.Models.Complaints;
import com.example.furniture.Models.OrderDetails;
import com.example.furniture.Models.Orders;
import com.example.furniture.Models.Products;
import com.example.furniture.Models.Response;
import com.example.furniture.Models.Universal;
import com.example.furniture.Models.Users;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser extends Response {

    private List<Users> userList;
    private List<Categories> categoriesList;
    private List<Products> productsList;
    private List<Orders> ordersList;
    private List<OrderDetails> orderDetailsList;
    private List<Complaints> complaintsList;

    public Parser() {
    }

    public static Parser parseResponse(String JSONResponse) {
        Parser parser = new Parser();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");
            parser.setCode(code);
            parser.setMessage(message);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parser;
    }

    public List<Users> getUsersList() {
        return userList;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    public void setUsersList(List<Users> userList) {
        this.userList = userList;
    }

    public List<Categories> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public List<Complaints> getComplaintsList() {
        return complaintsList;
    }

    public void setComplaintsList(List<Complaints> complaintsList) {
        this.complaintsList = complaintsList;
    }

    public static Parser parseUsers(String JSONResponse) {
        Parser parser = new Parser();
        List<Users> userList = new ArrayList<Users>();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");

            if(jsonObject.has("data")) {
                Users user = new Users();
                JSONObject data = jsonObject.getJSONObject("data");
                user.setId(data.getInt("id"));
                user.setLevel(data.getInt("level"));
                user.setUsername(data.getString("username"));
                user.setFullname(data.getString("fullname"));
                user.setEmail(data.getString("email"));
                user.setPassword(data.getString("password"));
                user.setPhone(data.getString("phone"));
                user.setCreated_date(data.getString("created_on"));
                userList.add(user);
            }

            parser.setCode(code);
            parser.setMessage(message);
            parser.setUsersList(userList);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parser;
    }

    public static Parser parseProducts(String JSONResponse) {
        Parser parser = new Parser();
        List<Products> productsList = new ArrayList<Products>();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");

            if(jsonObject.has("data")) {
                Products products = new Products();
                JSONObject data = jsonObject.getJSONObject("data");

                products.setId(data.getInt("id"));
                products.setCategory_id(data.getInt("category_id"));
                products.setCode(data.getString("code"));
                products.setName(data.getString("name"));
                products.setDescription(data.getString("description"));
                products.setStock(data.getInt("stock"));
                products.setPrice(data.getInt("price"));
                products.setAttachment(data.getString("attachment"));
                productsList.add(products);
            }

            parser.setCode(code);
            parser.setMessage(message);
            parser.setProductsList(productsList);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parser;
    }

    public List<Products> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Products> productsList) {
        this.productsList = productsList;
    }

    public static Parser parseCategory(String JSONResponse) {
        Parser parser = new Parser();
        List<Categories> categoriesList = new ArrayList<Categories>();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");

            if(jsonObject.has("data")) {
                Categories categories = new Categories();
                JSONObject data = jsonObject.getJSONObject("data");
                categories.setId(data.getInt("id"));
                categories.setCategory_name(data.getString("category_name"));
                categories.setCategory_icon(data.getString("category_icon"));
                categories.setCategory_code(data.getString("category_code"));
                categories.setCategory_index(data.getInt("category_index"));
                categoriesList.add(categories);
            }

            parser.setCode(code);
            parser.setMessage(message);
            parser.setCategoriesList(categoriesList);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parser;
    }

    public static Parser parseComplaints(String JSONResponse) {
        Parser parser = new Parser();
        List<Complaints> complaintsList = new ArrayList<Complaints>();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");

            if(jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i = 0; i < jsonArray.length(); i++) {
                    Complaints complaints = new Complaints();
                    JSONObject data = jsonArray.getJSONObject(i);
                    complaints.setId(data.getInt("id"));
                    complaints.setOrder_code(data.getString("order_code"));
                    complaints.setUser_id(data.getInt("user_id"));
                    complaints.setImage(data.getString("image"));
                    complaints.setInformation(data.getString("information"));
                    complaints.setStatus(data.getInt("status"));
                    complaints.setCreated_on(data.getString("created_on"));
                    complaints.setBuyer_name(data.getString("buyer_name"));
                    complaints.setBuyer_address(data.getString("buyer_address"));
                    complaints.setBuyer_phone(data.getString("buyer_phone"));
                    complaintsList.add(complaints);
                }
            }

            parser.setCode(code);
            parser.setMessage(message);
            parser.setComplaintsList(complaintsList);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parser;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public static Parser parseOrders(String JSONResponse) {
        Parser parser = new Parser();
        List<Orders> ordersList = new ArrayList<Orders>();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");

            if(jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i = 0; i < jsonArray.length(); i++) {
                    Orders orders = new Orders();
                    JSONObject data = jsonArray.getJSONObject(i);
                    orders.setId(data.getInt("id"));
                    orders.setUser_id(data.getInt("user_id"));
                    orders.setCode(data.getString("code"));
                    orders.setBuyer_name(data.getString("buyer_name"));
                    orders.setBuyer_address(data.getString("buyer_address"));
                    orders.setBuyer_phone(data.getString("buyer_phone"));
                    orders.setTotal(data.getInt("total"));
                    orders.setStatus(data.getInt("status"));
                    orders.setCreated_on(data.getString("created_on"));
                    ordersList.add(orders);
                }
            }

            parser.setCode(code);
            parser.setMessage(message);
            parser.setOrdersList(ordersList);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parser;
    }

    public static Parser parseOrderDetails(String JSONResponse) {
        Parser parser = new Parser();
        List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");

            if(jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i = 0; i < jsonArray.length(); i++) {
                    OrderDetails orders = new OrderDetails();
                    JSONObject data = jsonArray.getJSONObject(i);
                    orders.setOrder_code(data.getString("order_code"));
                    orders.setProduct_code(data.getString("product_code"));
                    orders.setProduct_price(data.getInt("product_price"));
                    orders.setQty(data.getInt("qty"));
                    orders.setProduct_name(data.getString("product_name"));
                    orders.setFotobayar(data.getString("fotobayar"));
                    orderDetails.add(orders);
                }
            }

            parser.setCode(code);
            parser.setMessage(message);
            parser.setOrderDetailsList(orderDetails);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parser;
    }

    public static Parser parseCodeResponse(String JSONResponse) {
        Parser parserResponse = new Parser();
        try{
            JSONObject jsonObject = new JSONObject(JSONResponse);

            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");

            parserResponse.setCode(code);
            parserResponse.setMessage(message);
        }
        catch (Exception ex) {
            Log.d("[DEBUG]", "Parsing error: " + ex.getMessage().toString());
        }
        return parserResponse;
    }

    public static List<Users> parseUsersList(String JSONResponse) {
        List<Users> userList = new ArrayList<Users>();
        Parser parser = new Parser();
        try {
            JSONArray jsonArray = new JSONArray(JSONResponse);

            for(int i = 0; i < jsonArray.length(); i++) {
                Users user = new Users();
                JSONObject data = jsonArray.getJSONObject(i);
                user.setId(data.getInt("id"));
                user.setLevel(data.getInt("level"));
                user.setUsername(data.getString("username"));
                user.setFullname(data.getString("fullname"));
                user.setEmail(data.getString("email"));
                user.setPassword(data.getString("password"));
                user.setPhone(data.getString("phone"));
                user.setCreated_date(data.getString("created_on"));
                userList.add(user);
            }
        }
        catch(Exception e) {
            Log.d("[DEBUG]", "Error throwed from UsersHelper -> " + e.getMessage().toString());
        }

        return userList;
    }

    public static List<Categories> parseCategoriesList(String JSONResponse) {
        List<Categories> categoriesList = new ArrayList<Categories>();
        Parser parser = new Parser();
        try {
            JSONArray jsonArray = new JSONArray(JSONResponse);

            for(int i = 0; i < jsonArray.length(); i++) {
                Categories categories = new Categories();
                JSONObject data = jsonArray.getJSONObject(i);
                categories.setId(data.getInt("id"));
                categories.setCategory_name(data.getString("category_name"));
                categories.setCategory_icon(data.getString("category_icon"));
                categories.setCategory_code(data.getString("category_code"));
                categories.setCategory_index(data.getInt("category_index"));
                Log.d("[DEBUG]", "Cat index parser: " + data.getInt("category_index"));
                categoriesList.add(categories);
            }
        }
        catch(Exception e) {
            Log.d("[DEBUG]", "Error throwed from UsersHelper -> " + e.getMessage().toString());
        }

        return categoriesList;
    }

    public static List<Products> parseProductsList(String JSONResponse) {
        List<Products> productsList = new ArrayList<Products>();
        Parser parser = new Parser();
        try {
            JSONArray jsonArray = new JSONArray(JSONResponse);

            for(int i = 0; i < jsonArray.length(); i++) {
                Products products = new Products();
                JSONObject data = jsonArray.getJSONObject(i);
                products.setId(data.getInt("id"));
                products.setCategory_id(data.getInt("category_id"));
                products.setCode(data.getString("code"));
                products.setName(data.getString("name"));
                products.setDescription(data.getString("description"));
                products.setStock(data.getInt("stock"));
                products.setPrice(data.getInt("price"));
                products.setAttachment(data.getString("attachment"));
                productsList.add(products);
            }
        }
        catch(Exception e) {
            Log.d("[DEBUG]", "Error throwed from UsersHelper -> " + e.getMessage().toString());
        }

        return productsList;
    }

    public static List<Orders> parseOrdersList(String JSONResponse) {
        List<Orders> ordersList = new ArrayList<Orders>();
        Parser parser = new Parser();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            if(jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i = 0; i < jsonArray.length(); i++) {
                    Orders orders = new Orders();
                    JSONObject data = jsonArray.getJSONObject(i);
                    orders.setId(data.getInt("id"));
                    orders.setUser_id(data.getInt("user_id"));
                    orders.setCode(data.getString("code"));
                    orders.setBuyer_name(data.getString("buyer_name"));
                    orders.setBuyer_address(data.getString("buyer_address"));
                    orders.setBuyer_phone(data.getString("buyer_phone"));
                    orders.setTotal(data.getInt("total"));
                    orders.setStatus(data.getInt("status"));
                    orders.setCreated_on(data.getString("created_on"));
                    ordersList.add(orders);
                }
            }
            parser.setOrdersList(ordersList);
        }
        catch(Exception e) {
            Log.d("[DEBUG]", "Error throwed from UsersHelper -> " + e.getMessage().toString());
        }

        return ordersList;
    }

    public static List<Universal> parseProvinsi(String JSONResponse) {
        List<Universal> universalList = new ArrayList<Universal>();
        Parser parser = new Parser();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            if(jsonObject.has("provinsi")) {
                JSONArray jsonArray = jsonObject.getJSONArray("provinsi");
                for(int i = 0; i < jsonArray.length(); i++) {
                    Universal universal = new Universal();
                    JSONObject data = jsonArray.getJSONObject(i);
                    universal.setId(data.getInt("id"));
                    universal.setObject_name(data.getString("nama"));
                    universal.setParent_id(0);

                    universalList.add(universal);
                }
            }
        }
        catch(Exception e) {
            Log.d("[DEBUG]", "Error throwed from UsersHelper -> " + e.getMessage().toString());
        }

        return universalList;
    }
    public static List<Universal> parseKabupaten(String JSONResponse) {
        List<Universal> universalList = new ArrayList<Universal>();
        Parser parser = new Parser();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            if(jsonObject.has("kota_kabupaten")) {
                JSONArray jsonArray = jsonObject.getJSONArray("kota_kabupaten");
                for(int i = 0; i < jsonArray.length(); i++) {
                    Universal universal = new Universal();
                    JSONObject data = jsonArray.getJSONObject(i);
                    universal.setId(data.getInt("id"));
                    universal.setObject_name(data.getString("nama"));
                    universal.setParent_id(data.getInt("id_provinsi"));

                    universalList.add(universal);
                }
            }
        }
        catch(Exception e) {
            Log.d("[DEBUG]", "Error throwed from UsersHelper -> " + e.getMessage().toString());
        }

        return universalList;
    }
    public static List<Universal> parseKecamatan(String JSONResponse) {
        List<Universal> universalList = new ArrayList<Universal>();
        Parser parser = new Parser();
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            if(jsonObject.has("kecamatan")) {
                JSONArray jsonArray = jsonObject.getJSONArray("kecamatan");
                for(int i = 0; i < jsonArray.length(); i++) {
                    Universal universal = new Universal();
                    JSONObject data = jsonArray.getJSONObject(i);
                    universal.setId(data.getInt("id"));
                    universal.setObject_name(data.getString("nama"));
                    universal.setParent_id(data.getInt("id_kota"));

                    universalList.add(universal);
                }
            }
        }
        catch(Exception e) {
            Log.d("[DEBUG]", "Error throwed from UsersHelper -> " + e.getMessage().toString());
        }

        return universalList;
    }
}
