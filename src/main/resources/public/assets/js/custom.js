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