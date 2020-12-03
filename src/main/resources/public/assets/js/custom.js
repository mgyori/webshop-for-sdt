//custom.js

"use strict";

/**== wow animation ==**/

new WOW().init();

/**== loader js ==*/

$(window).load(function() {
    $(".bg_load").fadeOut("slow");

    $("#cart").click(function(e) {
      $(".shopping-cart").fadeToggle( "fast");
      e.preventDefault();
      return false;
    });

});

/**== Menu js ==**/

$("#navbar_menu").menumaker({
	title: "Menu",
	format: "multitoggle"
});
	
/** header fixed js **/

$(window).scroll(function(){
    if ($(window).scrollTop() >= 300) {
       $('.header_fixed_on_scroll').addClass('fixed-header');
    }
    else {
       $('.header_fixed_on_scroll').removeClass('fixed-header');
    }
});

var waitForResponse = false;

function removeItem(e) {
	var t = $(this);
	$.post("/removeCartItem/" + $(this).data("remove"), {}, function(resp) {
		console.log(resp);
		t.parent().parent().remove();
		if (resp.length == 0) {
			$(".shopping-cart").fadeOut();
			$(".cartnav").removeClass("show").addClass("hidden");
		}
		var count = 0;
		var price = 0;
		for (var k in resp) {
			var cart = resp[k];
			count+=cart.count;
			price+=cart.count * cart.item.price;
		}
		$(".cartCount").text(count);
		$(".cartPrice").text(price);

	});
	e.preventDefault();
	return false;
}

if ($("#addToCard").length > 0) {
	$("#addToCard").submit(function(e) {
		if (waitForResponse)
		return;

		var c = $(this).find("[name='count']").val();
		var item = parseInt($(this).find("button").data("id"));
		waitForResponse = true;

		$.post(location.href, {count: c}, function(resp) {
			waitForResponse = false;
			var added = false;
			if (resp) {
				for (var k in resp) {
					var data = resp[k];
					if (data.item.id == item) {
						added = true;
						break;
					}
				}
			}
			if (added) {
				console.log(resp);
				$(".shopping-cart-items").remove();
				var count = 0;
				var items = '';
				var price = 0;
				for (var k in resp) {
					var cart = resp[k];
					items += `<ul class="shopping-cart-items">
								<li class="clearfix">
									<table>
										<td><img class="img-responsive" src="/assets/images/items/` + cart.item.image + `.jpg" height="70px" width="70px" alt=""></td>
										<td>
											<span class="item-name">` + cart.item.name + `</span><br>
											<span class="item-price"><span>` + cart.item.price + `</span> Ft</span>
											<span class="item-quantity">x<span>` + cart.count + `</span></span>
											<span data-remove="` + cart.item.id + `" class="item-delete"><i class="fa fa-trash"></i></span>
										</td>
									</table>
								</li>
							</ul>`;
					count+=cart.count;
					price+=cart.count * cart.item.price;
				}
				$(".shopping-cart-header").after(items);
				$(".cartCount").text(count);
				$(".cartPrice").text(price);
				$(".cartnav").addClass("show").removeClass("hidden");

				$("[data-remove]").click(removeItem);
			} else {
				alert("Valami hiba van...");
			}
		});
		e.preventDefault();
		return false;
	});
}

$("[data-remove]").click(removeItem);

// Képek betöltése
if ($(".product_detail_feature_img").length > 0) {
	var link = "https://markgyori.eu/web_images/";
	$.getJSON(link + "index.php?method=list&id=" + $(".product_detail_feature_img").data("id"), function(data) {
		var images = "";
		for (var v in data) {
			images += `<div class="product_img">
						<img class="img-responsive" src="` + link + data[v] + `" alt=""/>
					</div>`;
		}
		$(".product_detail_feature_img").append(images);
		$('.product_detail_feature_img').slick();
	});
}

if ($(".orderForm").length > 0) {
	$(".orderForm").submit(function(e) {
		var firstName = $(this).find("[name=firstName]").val();
		var lastName = $(this).find("[name=lastName]").val();
		var address = $(this).find("[name=address]").val();
		var ship_firstName = $(this).find("[name=ship_firstName]").val();
		var ship_lastName = $(this).find("[name=ship_lastName]").val();
		var ship_address = $(this).find("[name=ship_address]").val();
		var phone = $(this).find("[name=phone]").val();
		var email = $(this).find("[name=email]").val();

		var errors = [];
		if (firstName.length == 0)
			errors.push("A számlázási vezetékneved nem lehet üres!");
		if (lastName.length == 0)
			errors.push("A számlázási keresztneved nem lehet üres!");
		if (address.length == 0)
			errors.push("A számlázási címed nem lehet üres!");
		if (ship_firstName.length == 0)
			errors.push("A szállítási vezetékneved nem lehet üres!");
		if (ship_lastName.length == 0)
			errors.push("A szállítási keresztneved nem lehet üres!");
		if (ship_address.length == 0)
			errors.push("A szállítási címed nem lehet üres!");
		if (phone.length == 0)
			errors.push("A telefon számod nem lehet üres!");
		if (email.length == 0)
			errors.push("Az email címed nem lehet üres!");
			
		if (errors.length > 0) {
			alert(errors.join("\n"));
			e.preventDefault();
			return false;
		}
	});
}