<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">

	<div class="row">

		<div class="col-lg-3">

			<%@include file="./shared/sidebar.jsp"%>

		</div>
		<!-- /.col-lg-3 -->

		<div class="col-lg-9">
			<div class="row">
				<form method="post" id="nt_impact_drill-form"
					action="${contextRoot}/parser/file">
					<table class="table">
						<thead>
							<tr>
								<th colspan="2">File Parsing
							</tr>
							<tr>
								<th>#</th>
								<th>Method Name</th>

							</tr>
						</thead>
						<tbody>

							<c:if test="${!empty sessionScope.parametersForParserHTML}">

								<c:forEach items="${sessionScope.parametersForParserHTML}"
									var="parsing">

									<c:if test="${parsing.key == 'html-yurkas-seriia-sevilja'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-yurkas-seriia-sevilja"
														value="yurkas-seriia-sevilja">
													</label>
												</div></td>
											<td>Юркас серия Севилья</td>
										</tr>
									</c:if>

									<c:if test="${parsing.key == 'profildoors-seriia-portas'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-profildoors-seriia-portas"
														value="profildoors-seriia-portas">
													</label>
												</div></td>
											<td>ProfilDoors серия Portas</td>
										</tr>
									</c:if>

									<c:if
										test="${parsing.key == 'html-profildoors-seriia-x-klassika'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-profildoors-seriia-x-klassika"
														value="profildoors-seriia-x-klassika">
													</label>
												</div></td>
											<td>ProfilDoors серия Х классик</td>
										</tr>
									</c:if>
									<c:if
										test="${parsing.key == 'html-profildoors-seriia-x-modern'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-profildoors-seriia-x-modern"
														value="profildoors-seriia-x-modern">
													</label>
												</div></td>
											<td>ProfilDoors серия Х модерн</td>
										</tr>
									</c:if>
									<c:if
										test="${parsing.key == 'html-profildoors-seriia-u-klassika'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-profildoors-seriia-u-klassika"
														value="profildoors-seriia-u-klassika">
													</label>
												</div></td>
											<td>ProfilDoors серия U классика</td>
										</tr>
									</c:if>
									<c:if
										test="${parsing.key == 'html-profildoors-seriia-u-modern'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-profildoors-seriia-u-modern"
														value="profildoors-seriia-u-modern">
													</label>
												</div></td>
											<td>ProfilDoors серия U модерн</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-profildoors-seriia-z'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-profildoors-seriia-z"
														value="profildoors-seriia-z">
													</label>
												</div></td>
											<td>ProfilDoors серия Z</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-profildoors-seriia-e'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-profildoors-seriia-e"
														value="profildoors-seriia-e">
													</label>
												</div></td>
											<td>ProfilDoors серия E</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'yurkas-seriia-kasaporte'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-yurkas-seriia-kasaporte"
														value="yurkas-seriia-kasaporte">
													</label>
												</div></td>
											<td>Юркас серия Касапорте</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-yurkas-seriia-siti'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-yurkas-seriia-siti" value="yurkas-seriia-siti">
													</label>
												</div></td>
											<td>Юркас серия Сити</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-ladora-seriia-jego'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-ladora-seriia-jego" value="ladora-seriia-jego">
													</label>
												</div></td>
											<td>Ladora серия Эго</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-odincovo-seriia-turin'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-odincovo-seriia-turin"
														value="odincovo-seriia-turin">
													</label>
												</div></td>
											<td>Одинцово серия Турин</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-high-gloss'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-high-gloss" value="high-gloss">
													</label>
												</div></td>
											<td>Глянцевые двери</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-massive'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-massive" value="doors-massive">
													</label>
												</div></td>
											<td>Двери из массива</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-shponirovanie'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-shponirovanie"
														value="doors-shponirovanie">
													</label>
												</div></td>
											<td>Двери шпонированные</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-emal-okrashenie'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-emal-okrashenie"
														value="doors-emal-okrashenie">
													</label>
												</div></td>
											<td>Двери эмаль (окрашенные)</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-3d'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-3d" value="doors-3d">
													</label>
												</div></td>
											<td>Двери 3D</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-mdf'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-mdf" value="doors-mdf">
													</label>
												</div></td>
											<td>Двери МДФ</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-pvh'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-pvh" value="doors-pvh">
													</label>
												</div></td>
											<td>Двери ПВХ</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-garmoshka'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-garmoshka" value="doors-garmoshka">
													</label>
												</div></td>
											<td>Двери раздвижные (гармошка)</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-steklianye'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-steklianye" value="doors-steklianye">
													</label>
												</div></td>
											<td>Двери стеклянные</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-skladnye'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-skladnye" value="doors-skladnye">
													</label>
												</div></td>
											<td>Двери складные</td>
										</tr>
									</c:if>
									<c:if test="${parsing.key == 'html-doors-nevidimki'}">
										<tr>
											<td><div class="form-check">
													<label class="form-check-label"> <input
														class="form-check-input" type="checkbox"
														name="file-doors-nevidimki" value="doors-nevidimki">
													</label>
												</div></td>
											<td>Скрытые двери</td>
										</tr>
									</c:if>

									<tr>
										<td colspan="2"><input
											class="btn btn-primary btn-lg btn-block" type="submit"
											value="Start parser file"></td>
									</tr>
								</c:forEach>
							</c:if>

							<!-- ///////////////////////////////// -->
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox" name="file-test"
											value="test">
										</label>
									</div></td>
								<td>TEST</td>
							</tr>
							<tr>
								<td colspan="2"><input
									class="btn btn-primary btn-lg btn-block" type="submit"
									value="Start parser file"></td>
							</tr>

							<!-- ///////////////////////////////// -->

						</tbody>
					</table>

				</form>
			</div>

			<div>
				<form action="UploadDownloadFileServlet" method="post"
					enctype="multipart/form-data">
					Select File to Upload:<input type="file" name="fileName"> <br>
					<input type="submit" value="Upload">
				</form>




			</div>



			<!-- /.row -->

		</div>

	</div>
	<!-- /.row -->

</div>
<!-- /.container -->
