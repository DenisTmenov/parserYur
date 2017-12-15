<div class="container">

	<div class="row">

		<div class="col-lg-3">

			<%@include file="./shared/sidebar.jsp"%>

		</div>
		<!-- /.col-lg-3 -->

		<div class="col-lg-9">
			<div class="row">

				<form method="post" action="${contextRoot}/parser">

					<p>
						<input class="btn btn-primary btn-lg btn-block" type="submit" name="htmlParserStart"
							value="Html parser">
					</p>

					<p>
						<input class="btn btn-primary btn-lg btn-block" type="submit" name="fileParserStart"
							value="File parser">
					</p>

				</form>
				</p>
			</div>
		</div>

	</div>
	<!-- /.row -->

</div>
<!-- /.container -->