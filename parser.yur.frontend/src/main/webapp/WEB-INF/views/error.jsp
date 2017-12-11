<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="css" value="/resources/css" />
<spring:url var="vendor" value="/resources/vendor" />

<c:set var="contextRoot" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">

<head>

<title>Coffee - ${title}</title>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="Denis">
<meta name="author" content="Denis">

<!-- Bootstrap core CSS -->
<link href="${vendor}/bootstrap/css/bootstrap.css" rel="stylesheet">
<!-- Bootstrap dataTables CSS -->

<!-- Custom styles for this template -->
<link href="${css}/error.css" rel="stylesheet">
<link href="${css}/docs.min.css" rel="stylesheet">

<!-- FontAwesome -->
<link href="${css}/font-awesome.css" rel="stylesheet">


</head>

<body>

	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="error-template">
					<h1>Oops!</h1>
					<h2>${errorTitle}</h2>
					<div class="error-details">
						<blockquote style="">${errorDescription}</blockquote>

					</div>
					<div class="error-actions">
						<a href="${contextRoot}/home" class="btn btn-primary btn-lg"><span
							class="fa fa-home"></span> Take Me Home </a>
					</div>
				</div>
			</div>
		</div>
	</div>


</body>


</html>