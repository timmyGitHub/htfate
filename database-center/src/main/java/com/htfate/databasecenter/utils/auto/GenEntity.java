package com.htfate.databasecenter.utils.auto;

import org.springframework.beans.factory.annotation.Value;

import java.sql.*;


@SuppressWarnings("ALL")
public class GenEntity {

    //数据库连接
    @Value("${gen.url}")
    private static String URL = "jdbc:mysql://localhost:3306/htframe?characterEncoding=UTF-8&serverTimezone=GMT%2B8";
    @Value("${gen.name}")
    private static String NAME = "root";
    @Value("${gen.pass}")
    private static String PASS = "timmy";
    @Value("${gen.driver}")
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 通过表名自动生实体类
     *
     * @param tableName  表名
     * @param type       xml 生成xml需要的字符串  entity 生成实体类需要是字符串
     * @param entityPath xml 生成需要的新增那栏的实体类路径
     * @return
     */
    public static String entityStr(String tableName, String type, String entityPath) {
        String[] colnames; // 列名数组
        String[] colTypes; //列名类型数组
        int[] colSizes; //列名大小数组
        StringBuilder s = new StringBuilder();

        //创建连接
        Connection con;
        //查要生成实体类的表
        String sql = "select * from " + tableName;
        PreparedStatement pStemt = null;
        try {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            con = DriverManager.getConnection(URL, NAME, PASS);
            pStemt = con.prepareStatement(sql);
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();   //统计列
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];
            for (int i = 0; i < size; i++) {
                colnames[i] = rsmd.getColumnName(i + 1).toLowerCase();
                colTypes[i] = rsmd.getColumnTypeName(i + 1);
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }
            if (type.equals("entity")) {  //生成实体类需要的字符串
                //设置属性
                for (int i = 0; i < colnames.length; i++) {
                    String col = colnames[i];
                    String name = "";
                    if (col.indexOf("_") != -1) {
                        //存在好多个横线的情况 所以需要循环去吧第一个字母转大写
                        name = col.split("_")[0];
                        String[] cols = col.split("_");
                        for (int h = 1; h < cols.length; h++) {
                            name += initcap(cols[h]);
                        }
                    } else {
                        name = col.split("_")[0];
                    }
                    s.append("    private " + sqlType2JavaType(colTypes[i]) + " " + name + ";\r\n");
                }
                //设置setget方法
	          /*for (int i=0;i<colnames.length;i++){
	        	  String col = colnames[i];
	        	  String setName = "";
	        	  String setSx = "";
	        	  if (col.indexOf("_") != -1){
	        		  setName = col.split("_")[0];
	        		  setSx = initcap(col.split("_")[0]);
	        		  String[] cols = col.split("_");
	        		  for (int h = 1;h < cols.length; h++){
	        			  setName += initcap(cols[h]);
	        			  setSx += initcap(cols[h]);
	        		  }
	        	  } else {
	        		  setName = col.split("_")[0];
	        		  setSx = initcap(col.split("_")[0]);
	        	  }
	        	  s.append("    public void set"+setSx+"("+sqlType2JavaType(colTypes[i])+" "+setName+") {\r\n");
	        	  s.append("        this."+setName+" = " + setName+";\r\n");
	        	  s.append("    }\r\n\r\n");
	        	  s.append("    public "+sqlType2JavaType(colTypes[i])+" get"+setSx+"(){\r\n");
	        	  s.append("        return " + setName +";\r\n");
	        	  s.append("    }\r\n\r\n");
	          }*/
            } else {  //生成xml需要的字符串
                s.append("    <resultMap id=\"BaseResultMap\" type=\"[entityPackage].[entityClass]\">\r\n");
                String id = "";
                StringBuilder likeWhere = new StringBuilder();
                StringBuilder adminLikeWhere = new StringBuilder();    // 管理员
                StringBuilder fieldWhere = new StringBuilder();
                StringBuilder adminFieldWhere = new StringBuilder();    // 管理员
                StringBuilder insertCloumn = new StringBuilder("		<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\r\n");
                StringBuilder batchInsertCloum = new StringBuilder("		<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\r\n");
                StringBuilder insertValue = new StringBuilder(" 		<trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\" >\r\n");
                StringBuilder batchInsertValue = new StringBuilder(" 		<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\r\n");
                StringBuilder updateSet = new StringBuilder("		<trim prefix=\"set\" suffixOverrides=\",\">\r\n");
                StringBuilder batchUpdate = new StringBuilder();
                for (int i = 0; i < colnames.length; i++) {
                    String col = colnames[i];
                    if (i == 0) {
                        id = col.toUpperCase();
                    }
                    String name = "";
                    if (col.split("_").length == 2) {
                        name = col.split("_")[0] + initcap(col.split("_")[1]);
                    } else if (col.split("_").length == 3) {
                        name = col.split("_")[0] + initcap(col.split("_")[1] + initcap(col.split("_")[2]));
                    } else {
                        name = col.split("_")[0];
                    }
                    String resultMap = i == 0 ? "id" : "result";
                    s.append("        <" + resultMap + " column=\"" + col.toUpperCase() + "\" property=\"" + name + "\" jdbcType=\"" + sqlType2JavaTypeXml(colTypes[i]) + "\" />\r\n");
                    String wherename = "";
                    if (col.split("_").length == 2) {
                        wherename = col.split("_")[0] + initcap(col.split("_")[1]);
                    } else if (col.split("_").length == 3) {
                        wherename = col.split("_")[0] + initcap(col.split("_")[1] + initcap(col.split("_")[2]));
                    } else {
                        wherename = col.split("_")[0];
                    }
                    String dohao = i < colnames.length - 1 ? "," : "";
                    // 如果是日期不与空作比较
                    if ("TIMESTAMP".equalsIgnoreCase(colTypes[i]) || "datetime".equalsIgnoreCase(colTypes[i]) || "date".equalsIgnoreCase(colTypes[i])) {
                        likeWhere.append("			<if test=\"" + wherename + " != null\">\r\n");
                        likeWhere.append("				and " + col.toUpperCase() + " LIKE CONCAT('%',#{" + wherename + "},'%')\r\n");
                        likeWhere.append("			</if>\r\n");


                        adminLikeWhere.append("			<if test=\"" + wherename + " != null\">\r\n");
                        adminLikeWhere.append("				and " + col.toUpperCase() + " LIKE CONCAT('%',#{" + wherename + "},'%')\r\n");
                        adminLikeWhere.append("			</if>\r\n");

                        fieldWhere.append("			<if test=\"" + wherename + " != null\">\r\n");
//					  fieldWhere.append("				and "+col.toUpperCase()+" = #{"+wherename+"}\r\n");
                        fieldWhere.append("				and " + col.toUpperCase() + " in \r\n");
                        fieldWhere.append("				<foreach collection=\"" + wherename + ".split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\">\r\n");
                        fieldWhere.append("					#{it}\r\n");
                        fieldWhere.append("				</foreach>\r\n");
                        fieldWhere.append("			</if>\r\n");

                        adminFieldWhere.append("			<if test=\"" + wherename + " != null\">\r\n");
//					  adminFieldWhere.append("				and "+col.toUpperCase()+" = #{"+wherename+"}\r\n");
                        adminFieldWhere.append("				and " + col.toUpperCase() + " in \r\n");
                        adminFieldWhere.append("				<foreach collection=\"" + wherename + ".split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\">\r\n");
                        adminFieldWhere.append("					#{it}\r\n");
                        adminFieldWhere.append("				</foreach>\r\n");
                        adminFieldWhere.append("			</if>\r\n");


                        insertCloumn.append("			<if test=\"" + wherename + " != null\">\r\n");
                        insertCloumn.append("				" + col.toUpperCase() + "" + dohao + "\r\n");
                        insertCloumn.append("			</if>\r\n");
//				  batchInsertCloum.append("			<if test=\"list."+wherename+" != null and list."+wherename+" != ''\">\r\n");
                        batchInsertCloum.append("				" + col.toUpperCase() + "" + dohao + "\r\n");
//				  batchInsertCloum.append("			</if>\r\n");
                        insertValue.append("			<if test=\"" + wherename + " != null\">\r\n");
                        insertValue.append(" 				#{" + wherename + ",jdbcType=" + sqlType2JavaTypeXml(colTypes[i]) + "}" + dohao + "\r\n");
                        insertValue.append("			</if>\r\n");
//				  batchInsertValue.append("			<if test=\"file."+wherename+" != null and file."+wherename+" != ''\">\r\n");
                        batchInsertValue.append(" 				#{file." + wherename + ",jdbcType=" + sqlType2JavaTypeXml(colTypes[i]) + "}" + dohao + "\r\n");
//				  batchInsertValue.append("			</if>\r\n");
                        // 最后加 delflag
                        if (i == (colnames.length - 1)) {
                            likeWhere.append("			and delflag = 0\r\n");
                            fieldWhere.append("			and delflag = 0\r\n");
                        }
                        if (i != 0) {
                            updateSet.append("			<if test=\"" + wherename + " != null\">" + col.toUpperCase() + "=#{" + wherename + "}" + dohao + "</if>\r\n");
                            // 批量新增
                            batchUpdate.append("			" + col.toUpperCase() + "= \r\n");
                            batchUpdate.append("			CASE\r\n");
                            batchUpdate.append("				" + id + " \r\n");
                            batchUpdate.append("			<foreach collection=\"list\" item=\"row\" separator=\" \">\r\n");
                            if (id.toLowerCase().split("_").length > 1) {
                                batchUpdate.append("				WHEN #{row." + id.toLowerCase().split("_")[0] + initcap(id.toLowerCase().split("_")[1]) + "} THEN #{row." + wherename + "}\r\n");
                            } else {
                                batchUpdate.append("				WHEN #{row." + id.toLowerCase() + "} THEN #{row." + wherename + "}\r\n");
                            }
                            batchUpdate.append("			</foreach>\r\n");
                            batchUpdate.append("			END" + dohao + "\r\n");
                        }
                    } else {
                        likeWhere.append("			<if test=\"" + wherename + " != null and " + wherename + " != ''\">\r\n");
                        likeWhere.append("				and " + col.toUpperCase() + " LIKE CONCAT('%',#{" + wherename + "},'%')\r\n");
                        likeWhere.append("			</if>\r\n");
                        if (i == (colnames.length - 1)) {
                            likeWhere.append("			and delflag = 0\r\n");
                        }
                        adminLikeWhere.append("			<if test=\"" + wherename + " != null and " + wherename + " != ''\">\r\n");
                        adminLikeWhere.append("				and " + col.toUpperCase() + " LIKE CONCAT('%',#{" + wherename + "},'%')\r\n");
                        adminLikeWhere.append("			</if>\r\n");

                        fieldWhere.append("			<if test=\"" + wherename + " != null and " + wherename + " != ''\">\r\n");
//					  fieldWhere.append("				and "+col.toUpperCase()+" = #{"+wherename+"}\r\n");
                        fieldWhere.append("				and " + col.toUpperCase() + " in \r\n");
                        fieldWhere.append("				<foreach collection=\"" + wherename + ".split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\">\r\n");
                        fieldWhere.append("					#{it}\r\n");
                        fieldWhere.append("				</foreach>\r\n");
                        fieldWhere.append("			</if>\r\n");

                        adminFieldWhere.append("			<if test=\"" + wherename + " != null and " + wherename + " != ''\">\r\n");
//					  adminFieldWhere.append("				and "+col.toUpperCase()+" = #{"+wherename+"}\r\n");
                        adminFieldWhere.append("				and " + col.toUpperCase() + " in \r\n");
                        adminFieldWhere.append("				<foreach collection=\"" + wherename + ".split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\">\r\n");
                        adminFieldWhere.append("					#{it}\r\n");
                        adminFieldWhere.append("				</foreach>\r\n");
                        adminFieldWhere.append("			</if>\r\n");

                        insertCloumn.append("			<if test=\"" + wherename + " != null and " + wherename + " != ''\">\r\n");
                        insertCloumn.append("				" + col.toUpperCase() + "" + dohao + "\r\n");
                        insertCloumn.append("			</if>\r\n");
//				  batchInsertCloum.append("			<if test=\"list."+wherename+" != null and list."+wherename+" != ''\">\r\n");
                        batchInsertCloum.append("				" + col.toUpperCase() + "" + dohao + "\r\n");
//				  batchInsertCloum.append("			</if>\r\n");
                        insertValue.append("			<if test=\"" + wherename + " != null and " + wherename + " != ''\">\r\n");
                        insertValue.append(" 				#{" + wherename + ",jdbcType=" + sqlType2JavaTypeXml(colTypes[i]) + "}" + dohao + "\r\n");
                        insertValue.append("			</if>\r\n");
//				  batchInsertValue.append("			<if test=\"file."+wherename+" != null and file."+wherename+" != ''\">\r\n");
                        batchInsertValue.append(" 				#{file." + wherename + ",jdbcType=" + sqlType2JavaTypeXml(colTypes[i]) + "}" + dohao + "\r\n");
//				  batchInsertValue.append("			</if>\r\n");
                        if (i == (colnames.length - 1)) {
                            likeWhere.append("			and delflag = 0\r\n");
                            fieldWhere.append("			and delflag = 0\r\n");
                        }
                        if (i != 0) {
                            updateSet.append("			<if test=\"" + wherename + " != null and " + wherename + " != ''\">" + col.toUpperCase() + "=#{" + wherename + "}" + dohao + "</if>\r\n");
                            // 批量新增
                            batchUpdate.append("			" + col.toUpperCase() + "= \r\n");
                            batchUpdate.append("			CASE\r\n");
                            batchUpdate.append("				" + id + " \r\n");
                            batchUpdate.append("			<foreach collection=\"list\" item=\"row\" separator=\" \">\r\n");
                            if (id.toLowerCase().split("_").length > 1) {
                                batchUpdate.append("				WHEN #{row." + id.toLowerCase().split("_")[0] + initcap(id.toLowerCase().split("_")[1]) + "} THEN #{row." + wherename + "}\r\n");
                            } else {
                                batchUpdate.append("				WHEN #{row." + id.toLowerCase() + "} THEN #{row." + wherename + "}\r\n");
                            }
                            batchUpdate.append("			</foreach>\r\n");
                            batchUpdate.append("			END" + dohao + "\r\n");
                        }
                    }

                }
                insertCloumn.append("		</trim>\r\n");
                batchInsertCloum.append("		</trim>\r\n");
                insertValue.append("		</trim>\r\n");
                batchInsertValue.append("		</trim>\r\n");
                updateSet.append("		</trim>\r\n");
                s.append("    </resultMap>\r\n\r\n");
                // list 查询
                s.append("    <!-- list查询 -->\r\n");
                s.append("    <select id=\"getList\"  parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">\r\n");
                s.append("        SELECT * FROM " + tableName.toLowerCase() + " <include refid=\"fieldWhere\" />\r\n");
                s.append("		ORDER BY display\r\n");
                s.append("		<if test=\"pageIndex != null and pageSize != null\">\r\n");
                s.append("			LIMIT #{pageIndex},#{pageSize}\r\n");
                s.append("		</if>\r\n");
                s.append("    </select>\r\n\r\n");
                // 管理员 list 查询
                s.append("    <!--管理员 list查询 -->\r\n");
                s.append("    <select id=\"getAdminList\"  parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">\r\n");
                s.append("        SELECT * FROM " + tableName.toLowerCase() + " <include refid=\"adminFieldWhere\" />\r\n");
                s.append("		ORDER BY display\r\n");
                s.append("		<if test=\"pageIndex != null and pageSize != null\">\r\n");
                s.append("			LIMIT #{pageIndex},#{pageSize}\r\n");
                s.append("		</if>\r\n");
                s.append("    </select>\r\n\r\n");
                // list 模糊查询
                s.append("    <!-- list 模糊查询 -->\r\n");
                s.append("    <select id=\"getListLike\"  parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">\r\n");
                s.append("        SELECT * FROM " + tableName.toLowerCase() + " <include refid=\"likeWhere\" />\r\n");
                s.append("		ORDER BY display\r\n");
                s.append("		<if test=\"pageIndex != null and pageSize != null\">\r\n");
                s.append("			LIMIT #{pageIndex},#{pageSize}\r\n");
                s.append("		</if>\r\n");
                s.append("    </select>\r\n\r\n");
                // 管理员 list 模糊查询
                s.append("    <!--管理员 list 模糊查询 -->\r\n");
                s.append("    <select id=\"getAdminListLike\"  parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">\r\n");
                s.append("        SELECT * FROM " + tableName.toLowerCase() + " <include refid=\"adminLikeWhere\" />\r\n");
                s.append("		ORDER BY display\r\n");
                s.append("		<if test=\"pageIndex != null and pageSize != null\">\r\n");
                s.append("			LIMIT #{pageIndex},#{pageSize}\r\n");
                s.append("		</if>\r\n");
                s.append("    </select>\r\n\r\n");
                // 计数查询
                s.append("	<!-- 计数 -->\r\n");
                s.append("	<select id=\"getCount\" parameterType=\"java.util.Map\" resultType=\"java.lang.Integer\">\r\n");
                s.append("		SELECT COUNT(" + id + ") as cont FROM " + tableName.toLowerCase() + " <include refid=\"fieldWhere\" />\r\n");
                s.append("	</select>\r\n\r\n");
                // 管理员 计数查询
                s.append("	<!--管理员 计数 -->\r\n");
                s.append("	<select id=\"getAdminCount\" parameterType=\"java.util.Map\" resultType=\"java.lang.Integer\">\r\n");
                s.append("		SELECT COUNT(" + id + ") as cont FROM " + tableName.toLowerCase() + " <include refid=\"adminFieldWhere\" />\r\n");
                s.append("	</select>\r\n\r\n");
                // 计数模糊查询
                s.append("	<!-- 模糊计数 -->\r\n");
                s.append("	<select id=\"getCountLike\" parameterType=\"java.util.Map\" resultType=\"java.lang.Integer\">\r\n");
                s.append("		SELECT COUNT(" + id + ") as cont FROM " + tableName.toLowerCase() + " <include refid=\"likeWhere\" />\r\n");
                s.append("	</select>\r\n\r\n");
                // 管理员 计数模糊查询
                s.append("	<!-- 模糊计数 -->\r\n");
                s.append("	<select id=\"getAdminCountLike\" parameterType=\"java.util.Map\" resultType=\"java.lang.Integer\">\r\n");
                s.append("		SELECT COUNT(" + id + ") as cont FROM " + tableName.toLowerCase() + " <include refid=\"adminLikeWhere\" />\r\n");
                s.append("	</select>\r\n\r\n");
                // 新增
                s.append("    <!-- 新增 -->\r\n");
                s.append("	<insert id=\"save\" parameterType=\"" + entityPath + "\" >\r\n");
                s.append("		INSERT INTO " + tableName.toLowerCase() + "\r\n");
                s.append(insertCloumn.toString());
                s.append(insertValue.toString() + "");
                s.append("	</insert>\r\n\r\n");
                // 批量新增
                s.append("    <!-- 批量新增 -->\n");
                s.append("	<insert id=\"batchSave\" parameterType=\"java.util.List\">\r\n");
                s.append("		INSERT INTO " + tableName.toLowerCase() + "\r\n");
                s.append(batchInsertCloum.toString());
                s.append("\r\n		VALUES\r\n");
                s.append("		<foreach collection=\"list\" item=\"file\" separator=\",\">\r\n");
                s.append(batchInsertValue.toString());
                s.append("\r\n        </foreach>\r\n");
                s.append("	</insert>\r\n\r\n");
                // 修改
                s.append("	<!--修改-->\r\n");
                s.append("	<update id=\"update\"  parameterType=\"" + entityPath + "\">\r\n");
                s.append("		UPDATE " + tableName.toLowerCase() + "\r\n");
                s.append(updateSet.toString());
                if (id.toLowerCase().split("_").length > 1) {
                    s.append("		WHERE " + id + "=#{" + id.toLowerCase().split("_")[0] + initcap(id.toLowerCase().split("_")[1]) + "}\r\n");
                } else {
                    s.append("		WHERE " + id + "=#{" + id.toLowerCase() + "}\r\n");
                }
                s.append("	</update>\r\n\r\n");
                // 批量修改
                s.append("	<!-- 批量修改-->\r\n");
                s.append("	<update id=\"batchUpdate\" parameterType=\"java.util.List\">\r\n");
                s.append("		UPDATE " + tableName.toLowerCase() + " \r\n");
                s.append("		SET \r\n");
                s.append(batchUpdate.toString());
                s.append("		WHERE " + id + " IN \r\n");
                s.append("		<foreach collection=\"list\" item=\"row\" open=\"(\" separator=\",\" close=\")\">\r\n");
                s.append("			#{row.id}\r\n");
                s.append("		</foreach>\r\n");
                s.append("	</update>\r\n\r\n");
                // 管理员 删除
                s.append("    <!--管理员 删除 -->\r\n");
                s.append("    <delete id=\"deleteAdminByIds\" parameterType=\"java.lang.String\"> \r\n");
                s.append("        DELETE FROM " + tableName.toLowerCase() + " WHERE " + id + " IN \r\n");
                s.append("        <foreach collection=\"ids.split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\"> \r\n");
                s.append("            #{it}\r\n");
                s.append("        </foreach>\r\n");
                s.append("    </delete>\r\n\r\n");
                // 删除
                s.append("    <!-- 删除 -->\r\n");
                s.append("    <update id=\"deleteByIds\" parameterType=\"java.lang.String\"> \r\n");
                s.append("        UPDATE " + tableName.toLowerCase() + "set delflag = 1 WHERE " + id + " IN \r\n");
                s.append("        <foreach collection=\"ids.split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\"> \r\n");
                s.append("            #{it}\r\n");
                s.append("        </foreach>\r\n");
                s.append("    </update>\r\n\r\n");
                // 管理员 条件删除
                s.append("    <!--管理员 条件删除 -->\r\n");
                s.append("    <delete id=\"deleteAdminByMap\" parameterType=\"java.util.Map\"> \r\n");
                s.append("        DELETE FROM " + tableName.toLowerCase() + " <include refid=\"adminFieldWhere\" />\r\n");
                s.append("    </delete>\r\n\r\n");
                // 条件删除
                s.append("    <!-- 条件删除 -->\r\n");
                s.append("    <update id=\"deleteByMap\" parameterType=\"java.util.Map\"> \r\n");
                s.append("        update " + tableName.toLowerCase() + "set delflag = 1 <include refid=\"fieldWhere\" />\r\n");
                s.append("    </update>\r\n\r\n");
                // 管理员 根据 id 查询
                s.append("    <!--管理员  通过id返回对象-->\r\n");
                s.append("	<select id=\"getAdminObjById\" parameterType=\"java.lang.String\" resultMap=\"BaseResultMap\">\r\n");
                s.append("		SELECT * FROM " + tableName.toLowerCase() + " WHERE " + id + " = #{id}\r\n");
                s.append("	</select>\r\n");
                // 根据 id 查询
                s.append("    <!-- 通过id返回对象-->\r\n");
                s.append("	<select id=\"getObjById\" parameterType=\"java.lang.String\" resultMap=\"BaseResultMap\">\r\n");
                s.append("		SELECT * FROM " + tableName.toLowerCase() + " WHERE " + id + " = #{id} \r\n");
                s.append("		and delflag=0");
                s.append("	</select>\r\n");
                // 管理员 根据多个 id 查询
                s.append("    <!--管理员  通过id返回对象-->\r\n");
                s.append("	<select id=\"getAdminObjByIds\" parameterType=\"java.lang.String\" resultMap=\"BaseResultMap\">\r\n");
                s.append("		SELECT * FROM " + tableName.toLowerCase() + " WHERE " + id + " IN\r\n");
                s.append("        <foreach collection=\"ids.split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\"> \r\n");
                s.append("            #{it}\r\n");
                s.append("        </foreach>\r\n");
                s.append("	</select>\r\n");
                // 根据多个 id 查询
                s.append("    <!-- 通过id返回对象-->\r\n");
                s.append("	<select id=\"getObjByIds\" parameterType=\"java.lang.String\" resultMap=\"BaseResultMap\">\r\n");
                s.append("		SELECT * FROM " + tableName.toLowerCase() + " WHERE " + id + " IN\r\n");
                s.append("        <foreach collection=\"ids.split(',')\" item=\"it\" open=\"(\" separator=\",\" close=\")\"> \r\n");
                s.append("            #{it}\r\n");
                s.append("        </foreach>\r\n");
                s.append("		and delflag=0");
                s.append("	</select>\r\n");
                // where liek 条件
                s.append("    <!-- where like 条件 -->\r\n");
                s.append("    <sql id=\"likeWhere\">\r\n");
                s.append("        <where>\r\n");
                s.append(likeWhere.toString());
                s.append("        </where>\r\n");
                s.append("    </sql>\r\n\r\n");
                // 管理员 where like 条件
                s.append("    <!--管理员 where like 条件 -->\r\n");
                s.append("    <sql id=\"adminLikeWhere\">\r\n");
                s.append("        <where>\r\n");
                s.append(adminLikeWhere.toString());
                s.append("        </where>\r\n");
                s.append("    </sql>\r\n\r\n");

                // where 条件
                s.append("    <!-- where 条件 -->\r\n");
                s.append("    <sql id=\"fieldWhere\">\r\n");
                s.append("        <where>\r\n");
                s.append(fieldWhere.toString());
                s.append("        </where>\r\n");
                s.append("    </sql>\r\n\r\n");
                // 管理员 where 条件
                s.append("    <!--管理员 where 条件 -->\r\n");
                s.append("    <sql id=\"adminFieldWhere\">\r\n");
                s.append("        <where>\r\n");
                s.append(adminFieldWhere.toString());
                s.append("        </where>\r\n");
                s.append("    </sql>\r\n\r\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //System.err.println("获取setGet出错");
        }
        return s.toString();
    }

    /**
     * 功能：将输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private static String initcap(String str) {

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }

    /**
     * 功能：获得列的数据类型 实体类的
     *
     * @param sqlType
     * @return
     */
    private static String sqlType2JavaType(String sqlType) {

        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney") || sqlType.equalsIgnoreCase("double")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("timestamp")) {
            return "Timestamp";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }
        return null;
    }

    private static String sqlType2JavaTypeXml(String sqlType) {
        if (sqlType.equalsIgnoreCase("int")) {
            return "INTEGER";
        } else if (sqlType.equalsIgnoreCase("double")) {
            return "DOUBLE";
        } else if (sqlType.equalsIgnoreCase("varchar")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "VARCHAR";
        } else if (sqlType.equalsIgnoreCase("char")) {
            return "CHAR";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "BIGINT";
        } else if (sqlType.equalsIgnoreCase("date")) {
            return "DATE";
        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("timestamp")) {
            return "TIMESTAMP";
        }
        return null;
    }
}