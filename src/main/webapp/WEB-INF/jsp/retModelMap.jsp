<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
	<title>retModelMap</title>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
</head>
<body>
<h2>retModelMap</h2>
${list}
${map}
<% for(int i=0; i < ((List)request.getAttribute("list")).size(); i++){
	out.print(((List)request.getAttribute("list")).get(i));
} %>
</body>
</html>