package javapiproj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class lottoservice {

    String uid = "javadmin";
    String pwd = "password";
    
    lottoservice() {
        System.out.println("Constructor");
    }

    String getname(String name) {
        //query the database and see if name exists if yes return the data if not save it and issue a lottery
        String retstr = "";
        if (isnameexists(name)) {
            //name exists fetch the pre issues lotto and issue a new one 
            System.out.println("" + name + " exists");
        } else {
            //issue a new one 
            System.out.println("" + name + " does not exists");
        }

        return retstr;
    }

    JSONArray getlotdtls(String name, Boolean buyval) {

        String retstr = "";
        //find the maximum issued lotto count 
        String prepqry = "select lastlotto from t_last_lotto limit 1";
        ResultSet rs = runQry(prepqry);
        int nextlottono = 0;
        try {
            while (rs.next()) {
                System.out.println(rs.getInt(1));
                nextlottono = Integer.parseInt(rs.getString(1));
                nextlottono++;
            }

            //insert nextlotto in 
            Date date = new Date();
            SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd");
            String dt = ft1.format(date);
          
             
            if (buyval) {
                String schedidval="";
                //find out the schedid to which these will go
                prepqry="SELECT schedid FROM scheddtl WHERE schdstatus='Pending' and rundttm =(select min(rundttm) from scheddtl where rundttm>CURRENT_TIMESTAMP)";
                
                rs=runQry(prepqry);
                while(rs.next()){
                    schedidval=rs.getString(1);
                }
                prepqry = "insert into lottoissue (name,lottono,issuedt,status,schedid) values  ('" + name + "','" + nextlottono + "','" + dt + "','Pending','" + schedidval+"')";
                int returnval = executeqry(prepqry);
                prepqry = "update t_last_lotto set lastlotto='" + nextlottono + "'";
                returnval = executeqry(prepqry);
                System.out.println(" return value after insertion " + returnval);
            }
           //prepqry = "select * from lottoissue where name='" + name + "'";
           prepqry = "select name,lottono,issuedt,status, concat(STR_TO_DATE (substring(schedid,1,8),'%d%m%Y') ,':' , substring(schedid,9,2),':00') as schedid from lottoissue where name='" + name + "'";
            rs = runQry(prepqry);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultsettojson(rs);
    }//end of getlotdtls

    boolean isnameexists(String name) {
        boolean nameexists = false;
        String prepqry = "select distinct 1 from lottoissue where name='" + name + "'";
        ResultSet rs = runQry(prepqry);
        try {
            while (rs.next()) {
                System.out.println(rs.getInt(1));
                if (rs.getInt(1) == 1) {
                    nameexists = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameexists;
    }

    ResultSet runQry(String qryString) {
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",uid,pwd);
            //here sonoo is database name, root is username and password  
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(qryString);
            /*
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            }
             */
            // con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }//end of executeqry

    int executeqry(String prepqry) {
        int returnint = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", uid,pwd);
            //here sonoo is database name, root is username and password  
            Statement stmt = con.createStatement();
            returnint = stmt.executeUpdate(prepqry);

        } catch (Exception e) {
            System.out.println(e);
        }
        return returnint;
    }//end of executeqry

    JSONArray resultsettojson(ResultSet resultSet) {
        String returnstr = "";
        JSONObject jsonobject = null;
        JSONArray jsonArray = new JSONArray();
        try {
            while (resultSet.next()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                jsonobject = new JSONObject();

                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    jsonobject.put(metaData.getColumnLabel(i + 1), resultSet.getString(i + 1));
                }
                jsonArray.add(jsonobject);
            }

            if (jsonArray.size() > 0) {
                returnstr = jsonArray.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // return returnstr;
          return jsonArray;
    }

}//end of class

