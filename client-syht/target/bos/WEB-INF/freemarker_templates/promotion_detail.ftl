<link rel="stylesheet" type="text/css" href="css/promotion_detail.css">
<div class="container promotions" >
	<div class="col-md-2 prolist">
		<h5 class="title"><a href="#/promotion"><strong>���ش����б�</strong></a></h5>
		<img src="images/pro.jpg" class="img-responsive">
	</div>
	<div class="col-md-10 procontent">
		<h5 class="title">${promotion.title}</h5>
		<div class="intro">
			<p>���Χ: ${promotion.activeScope}</p>
			<p>���Χ: ${promotion.startDate?string("yyyy-MM-dd")} - ${promotion.endDate?string("yyyy-MM-dd")}</p>
			<#--freemarker����������,����ָ����ʽ�� -->
		</div>
		<div class="partline clearfix"></div>
		<div class="promotionbox" ng-bind-html-unsafe="globalItem.description">
		${promotion.description}
		</div>
	</div>
</div>
