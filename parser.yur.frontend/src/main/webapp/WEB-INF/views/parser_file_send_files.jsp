<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="springForm"
	uri="http://www.springframework.org/tags/form"%>

<form action="${contextRoot}/upload/file" method="post"
	enctype="multipart/form-data">
	<div>
		<h2>Choose a files for upload:</h2>
	</div>
	<br>
	<div>
		<label for="file">Select SQL File to Upload: </label> <input
			type="file" class="form-control-file" id="sqlFile" name="sqlFile"
			aria-describedby="sqlFileHelp">
		<c:if test="${not empty uploadFiles.errors.sql}">
			<small id="sqlFileHelp" class="form-text text-muted">${uploadFiles.errors.sql}</small>
		</c:if>
	</div>
	<br>
	<div>
		<label for="file">Select TXT File to Upload: </label> <input
			type="file" class="form-control-file" id="txtFile" name="txtFile"
			aria-describedby="txtFileHelp">
		<c:if test="${not empty uploadFiles.errors.txt}">
			<small id="txtFileHelp" class="form-text text-muted">${uploadFiles.errors.txt}</small>
		</c:if>
	</div>
	<br>
	<div>
		<input type="text" name="files-upload" value="test" hidden="hidden">
		<input class="btn btn-primary btn-lg btn-block" type="submit"
			value="Upload files">
	</div>
</form>