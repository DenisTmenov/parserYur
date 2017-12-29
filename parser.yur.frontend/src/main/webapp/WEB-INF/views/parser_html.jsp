<div class="container">

	<div class="row">

		<div class="col-lg-3">

			<%@include file="./shared/sidebar.jsp"%>

		</div>
		<!-- /.col-lg-3 -->

		<div class="col-lg-9">
			<div class="row">
				<form method="post" action="${contextRoot}/parser/html">
					<table class="table">
						<thead>
							<tr>
								<th colspan="2">HTML Parsing
							</tr>
							<tr>
								<th>#</th>
								<th>Method Name</th>

							</tr>
						</thead>
						<tbody>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="html-yurkas-mezhkomnatnye-dveri"
											value="html-yurkas-mezhkomnatnye-dveri">
										</label>
									</div></td>
								<td>Юркас Межкомнатные двери</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="html-yurkas-vhodnye-dveri"
											value="html-yurkas-vhodnye-dveri">
										</label>
									</div></td>
								<td>Юркас Входные двери</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="html-test"
											value="html-test">
										</label>
									</div></td>
								<td>TEST</td>
							</tr>
							<tr>
								<td colspan="2"><input
									class="btn btn-primary btn-lg btn-block" type="submit"
									value="Start parser html"></td>
							</tr>

						</tbody>
					</table>

				</form>
			</div>

			<!-- /.row -->

		</div>

	</div>
	<!-- /.row -->

</div>
<!-- /.container -->

