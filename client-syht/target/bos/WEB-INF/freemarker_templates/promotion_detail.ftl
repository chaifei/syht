<link rel="stylesheet" type="text/css" href="css/promotion_detail.css">
<div class="container promotions" >
	<div class="col-md-2 prolist">
		<h5 class="title"><a href="#/promotion"><strong>返回促销列表</strong></a></h5>
		<img src="images/pro.jpg" class="img-responsive">
	</div>
	<div class="col-md-10 procontent">
		<h5 class="title">${promotion.title}</h5>
		<div class="intro">
			<p>活动范围: ${promotion.activeScope}</p>
			<p>活动范围: ${promotion.startDate?string("yyyy-MM-dd")} - ${promotion.endDate?string("yyyy-MM-dd")}</p>
			<#--freemarker的日期类型,必须指定格式化 -->
		</div>
		<div class="partline clearfix"></div>
		<div class="promotionbox" ng-bind-html-unsafe="globalItem.description">
		${promotion.description}
		</div>
	</div>
</div>
