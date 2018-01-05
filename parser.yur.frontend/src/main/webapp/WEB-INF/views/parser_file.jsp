<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="springForm"
	uri="http://www.springframework.org/tags/form"%>
<div class="container">

	<div class="row">

		<div class="col-lg-3">

			<%@include file="./shared/sidebar.jsp"%>

		</div>
		<!-- /.col-lg-3 -->

		<div class="col-lg-9">
			<div class="row">
					<c:choose>
						<c:when test="${not empty categories}">
							<%@include file="parser_file_choose_category.jsp"%>
						</c:when>

						<c:otherwise>
							<%@include file="parser_file_send_files.jsp"%>
						</c:otherwise>
					</c:choose>
			</div>

			<!-- /.row -->

		</div>

	</div>
	<!-- /.row -->

</div>
<!-- /.container -->
