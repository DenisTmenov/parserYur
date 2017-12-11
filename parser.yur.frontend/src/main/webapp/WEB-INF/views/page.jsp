<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="css" value="/resources/css" />
<spring:url var="images" value="/resources/images" />
<spring:url var="js" value="/resources/js" />
<spring:url var="jquery" value="/resources/jquery" />

<c:set var="contextRoot" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="Denis">
<meta name="author" content="Denis">

<title>Parser - ${title}</title>

<script>
	window.menu = '${title}';
	
	window.contextRoot = '${contextRoot}';
</script>

<!-- Bootstrap core CSS -->
<link href="${css}/bootstrap.css" rel="stylesheet">
<!-- Bootstrap dataTables CSS -->
<link href="${css}/dataTables.bootstrap4.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="${css}/page.css" rel="stylesheet">
<link href="${css}/docs.min.css" rel="stylesheet">

<!-- FontAwesome -->
<link href="${css}/font-awesome.css" rel="stylesheet">

</head>

<body>

	<div class="wrapper">
		<!-- Navigation -->
		<%@include file="./shared/navbar.jsp"%>


		<!-- Page Content start -->
		<div class="content">
			<!-- Home page -->
			<c:if test="${userClickHome == true}">
				<%@include file="home.jsp"%>
			</c:if>

			<!-- About page -->
			<c:if test="${userClickAbout == true}">
				<%@include file="about.jsp"%>
			</c:if>

			<!-- Contacts page -->
			<c:if test="${userClickContacts == true}">
				<%@include file="contacts.jsp"%>
			</c:if>

			<!-- Services page -->
			<c:if test="${userClickServices == true}">
				<%@include file="services.jsp"%>
			</c:if>
			
			<!-- Parser page -->
			<c:if test="${userClickParser == true}">
				<%@include file="parser.jsp"%>
			</c:if>
			
			
		</div>
		<!-- Page Content end -->

		<!-- Footer -->
		<%@include file="./shared/footer.jsp"%>

		<!-- Bootstrap core JavaScript -->
		<script src="${js}/popper.js"></script>
		<script src="${js}/bootstrap.js"></script>
		
		<!-- Bootstrap dataTables JavaScript -->
		<script src="${js}/dataTables.bootstrap4.js"></script>
		
		<!-- jQuery -->
		<script src="${jquery}/jquery.js"></script>
		
		<!-- Custom scripts -->
		<script src="${js}/userApp.js"></script>
		
		<!-- DataTable Plugin -->
		<script src="${jquery}/jquery.dataTables.js"></script>
		
		
	</div>
</body>

</html>
<!-- http://fontawesome.io/icons/ -->
