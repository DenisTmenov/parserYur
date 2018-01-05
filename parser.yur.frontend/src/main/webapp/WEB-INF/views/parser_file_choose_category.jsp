<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form action="${contextRoot}/categories" method="post"
	enctype="multipart/form-data">
	<div>
		<h2>Choose a category in which there will be doors:</h2>

		<c:forEach items="${categories}" var="category">

			<div class="form-check">
				<label class="form-check-label"> <input type="checkbox"
					class="form-check-input" id="check${category.value}"
					name="check${category.value}" value="${category.value}">
					${category.key}
				</label>
			</div>

		</c:forEach>

		<div>
			<input class="btn btn-primary btn-lg " type="submit" value="Submit categories"
				name="choose-categories"> &emsp; <input
				class="btn btn-primary btn-lg " type="submit" value="Submit without categories"
				name="choose-empty"> &emsp; <input
				class="btn btn-primary btn-lg " type="submit" value="Back to upload files"
				name="choose-back">
		</div>
	</div>
</form>

