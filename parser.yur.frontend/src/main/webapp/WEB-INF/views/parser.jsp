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
					action="${contextRoot}/parser">
					<table class="table">
						<thead>
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
											name="yurkas-seriia-sevilja" value="yurkas-seriia-sevilja">
										</label>
									</div></td>
								<td>Юркас серия Севилья</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="profildoors-seriia-portas"
											value="profildoors-seriia-portas">
										</label>
									</div></td>
								<td>ProfilDoors серия Portas</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="profildoors-seriia-x-klassika"
											value="profildoors-seriia-x-klassika">
										</label>
									</div></td>
								<td>ProfilDoors серия Х классик</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="profildoors-seriia-x-modern"
											value="profildoors-seriia-x-modern">
										</label>
									</div></td>
								<td>ProfilDoors серия Х модерн</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="profildoors-seriia-u-klassika"
											value="profildoors-seriia-u-klassika">
										</label>
									</div></td>
								<td>ProfilDoors серия U классика</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="profildoors-seriia-u-modern"
											value="profildoors-seriia-u-modern">
										</label>
									</div></td>
								<td>ProfilDoors серия U модерн</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="profildoors-seriia-z" value="profildoors-seriia-z">
										</label>
									</div></td>
								<td>ProfilDoors серия Z</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="profildoors-seriia-e" value="profildoors-seriia-e">
										</label>
									</div></td>
								<td>ProfilDoors серия E</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="yurkas-seriia-kasaporte"
											value="yurkas-seriia-kasaporte">
										</label>
									</div></td>
								<td>Юркас серия Касапорте</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="yurkas-seriia-siti" value="yurkas-seriia-siti">
										</label>
									</div></td>
								<td>Юркас серия Сити</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="ladora-seriia-jego" value="ladora-seriia-jego">
										</label>
									</div></td>
								<td>Ladora серия Эго</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="odincovo-seriia-turin" value="odincovo-seriia-turin">
										</label>
									</div></td>
								<td>Одинцово серия Турин</td>
							</tr>
							<tr></tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox" name="high-gloss"
											value="high-gloss">
										</label>
									</div></td>
								<td>Глянцевые двери</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox" name="doors-massive"
											value="doors-massive">
										</label>
									</div></td>
								<td>Двери из массива</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="doors-shponirovanie" value="doors-shponirovanie">
										</label>
									</div></td>
								<td>Двери шпонированные</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="doors-emal-okrashenie" value="doors-emal-okrashenie">
										</label>
									</div></td>
								<td>Двери эмаль (окрашенные)</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox" name="doors-3d"
											value="doors-3d">
										</label>
									</div></td>
								<td>Двери 3D</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox" name="doors-mdf"
											value="doors-mdf">
										</label>
									</div></td>
								<td>Двери МДФ</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox" name="doors-pvh"
											value="doors-pvh">
										</label>
									</div></td>
								<td>Двери ПВХ</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="doors-garmoshka" value="doors-garmoshka">
										</label>
									</div></td>
								<td>Двери раздвижные (гармошка)</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="doors-steklianye" value="doors-steklianye">
										</label>
									</div></td>
								<td>Двери стеклянные</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="doors-skladnye" value="doors-skladnye">
										</label>
									</div></td>
								<td>Двери складные</td>
							</tr>
							<tr>
								<td><div class="form-check">
										<label class="form-check-label"> <input
											class="form-check-input" type="checkbox"
											name="doors-nevidimki" value="doors-nevidimki">
										</label>
									</div></td>
								<td>Скрытые двери</td>
							</tr>


							<tr>
								<td colspan="2"><input class="btn btn-primary btn-lg btn-block"
									type="submit" value="Start parser"></td>
							</tr>

						</tbody>
					</table>

				</form>
			</div>

			<!-- /.row -->

		</div>
		<!-- /.col-lg-9 -->

	</div>
	<!-- /.row -->

</div>
<!-- /.container -->