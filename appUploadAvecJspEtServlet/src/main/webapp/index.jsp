<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Upload File with Java EE</h2>
	<form action="UploadFile" method="post" enctype="multipart/form-data">
		<input type="file" name="fichier"><br/>
		<input type="text" name="destination" value="/temp"><br/>
		<input type="submit" value="Envoyer">
	</form>
	<br/>
	<%
		String filename=(String)request.getAttribute("filename");
	%>
	<%if (filename.length() > 0){ %>
		Le fichier <%=filename %> a été bien envoyé !
	<%}%>
</body>
</html>
