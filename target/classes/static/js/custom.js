$(document).ready(function(){
"use strict";

// Mobile Menu
$('.js-click-megamenu').on('click', function(event) {
    $(".box-mobile-menu").addClass('open')
    $(".menu-overlay").fadeIn();
    event.preventDefault()
});
$('.menu-overlay').on('click', function(event) {
    $(".box-mobile-menu").removeClass('open')
    $(".menu-overlay").fadeOut();
    event.preventDefault()
});
$('.close-menu').on('click', function(event) {
    $(".box-mobile-menu").removeClass('open')
    $(".menu-overlay").fadeOut();
    event.preventDefault()
});

// Toggle submenu mobile / responsive
$(document).on('click', '.menu-item .toggle-submenu', function (e) {
    var $this = $(this);
    var $thisParent = $this.closest('.menu-item-has-children');
    if ($thisParent.length) {
        $thisParent.toggleClass('show-submenu').find('> .submenu').stop().slideToggle();
    }
    e.preventDefault();
    return false;
});

//------- Client say page_01 --------//  
$('.active-review-carusel').owlCarousel({
    items:1,
    loop:true,
    autoplay:true,
    autoplayHoverPause: true,        
    margin:30,
    dots: true
});

 $('.active-clientsaylist').owlCarousel({
        items: 2,
        loop: true,
        autoplayHoverPause: true,
        dots: false,
        autoplay: false,
        nav: true,
        navText: ["<span class='lnr lnr-arrow-up'></span>", "<span class='lnr lnr-arrow-down'></span>"],
        responsive: {
            0: {
                items: 1
            },
            480: {
                items: 1,
            },
            768: {
                items: 1,
            }
        }
    });

$('.active-brand-carusel').owlCarousel({
    items: 5,
    loop: true,
    autoplayHoverPause: true,
    autoplay: true,
    responsive: {
        0: {
            items: 1
        },
        455: {
            items: 2
        },            
        768: {
            items: 3,
        },
        991: {
            items: 4,
        },
        1024: {
            items: 5,
        }
    }
});


//Scroll Menu page_02
// document.addEventListener("DOMContentLoaded",function() {
//     var menu = document.querySelectorAll('section.header-bottom');
//     var menu = menu[0];
//         //Truy xuất div menu
//         var status="duoi150";
//     window.addEventListener("scroll",function(){
//         var x = pageYOffset;
//         if(x > 150){
//             if(status == "duoi150")
//             {
//                 status="tren150";
//                 menu.classList.add('menutora');
//             }
//         }
//         else{
//             if(status=="tren150"){
//             menu.classList.remove('menutora');
//             status="duoi150";}
//         }
    
//     })
// })

// Search box homepage 1
$('.js-search-box').on('click', function(event) {
    $(".search-box").addClass('search-box-active')
    $(".st1-search-box-bg").addClass('search-box-bg-active')
    event.preventDefault()
});
$('.close-search-box').on('click', function(event) {
    $(".search-box").removeClass('search-box-active')
    $(".st1-search-box-bg").removeClass('search-box-bg-active')
    event.preventDefault()
});
$('.st1-search-box-bg').on('click', function(event) {
    $(".search-box").removeClass('search-box-active')
    $(".st1-search-box-bg").removeClass('search-box-bg-active')
    event.preventDefault()
});

// Minicart
$('.js-click-cart').on('click', function(event) {
    $(".hamadryad-minicart").addClass('open')
    $(".minicart-bg-overlay").fadeIn('slow');
    event.preventDefault()
});
$('.hamadryad-minicart-close').on('click', function(event) {
    $(".hamadryad-minicart").removeClass('open')
    $(".minicart-bg-overlay").fadeOut('slow');
    event.preventDefault()
});
$('.minicart-bg-overlay').on('click', function(event) {
    $(".hamadryad-minicart").removeClass('open')
    $(".minicart-bg-overlay").fadeOut('slow');
    event.preventDefault()
});

 // Toggle submenu categories
$(document).on('click', '.st3-categories-item .st3-categories-icon', function (e) {
    var $this = $(this);
    var $thisParent = $this.closest('.st3-categories-item-child');
    if ($thisParent.length) {
        $thisParent.toggleClass('show-subcate').find('> .sub-categories').stop().slideToggle();
    }
    e.preventDefault();
    return false;
});

// Slide Page 1
function slideShow1() {
    var $status = $('.pagingInfo');
    var $slickElement = $('.slide-homepage');
    $slickElement.on('init reInit afterChange', function(event, slick, currentSlide, nextSlide) {
    //currentSlide is undefined on init -- set it to 0 in this case (currentSlide is 0 based)
    var i = (currentSlide ? currentSlide : 0) + 1;
    $status.html('<div class="pagingInfo">0' + i + '</div>');
    });
    $('.slide-homepage').on('afterChange', function(event, slick, currentSlide) {
    $('.slick-active').append('<div class="pagingInfo"');
    });

    $('.slide-homepage').slick({
        dots:false,
        arrows: true,
        infinite: true,
        autoplay: true,
        asNavFor: '.js-bg-slide-homepage1, .js-slide-avatar',
        autoplaySpeed:5000,
        slidesToShow: 1,
        speed: 1000,
        fade: false,
        customPaging : function(slider, i) {
        var thumb = $(slider.$slides[i]).data();
        return '<a>0'+(i+1)+'</a>';
        },
    });

    $('.js-bg-slide-homepage1').slick({
        dots:false,
        arrows: false,
        infinite: true,
        autoplay: true,
        asNavFor: '.slide-homepage, .js-slide-avatar',
        autoplaySpeed:5000,
        slidesToShow: 1,
        speed: 1000,
        fade: false,
    });

    $('.js-slide-avatar').slick({
        dots:false,
        arrows: false,
        infinite: true,
        autoplay: true,
        asNavFor: '.slide-homepage, .js-bg-slide-homepage1',
        autoplaySpeed:5000,
        slidesToShow: 1,
        speed: 1000,
        fade: false,
    });
}
slideShow1();

$('.js-slide-previous').on('click', function(e){
    $('.slick-prev').click();
    e.preventDefault(); 
});

$('.js-slide-next').on('click', function(e){
    $('.slick-next').click();
    e.preventDefault(); 
});

// Sider Page3
$('.categories-slide-right').slick({
    dots: false,
    arrows: false,
    autoplay: false,
    autoplaySpeed:1500
});


// Sider About us
$('.slide-aboutus').slick({
    dots: false,
    arrows: false,
    autoplay: true,
    autoplaySpeed:1500
});

// Slide Page 2
$('.slide-page2').slick({
    dots: false,
    arrows: false,
    autoplay: false,
    autoplaySpeed:1500
});

// Sider About us
$('.slide-contactus').slick({
    dots: false,
    arrows: false,
    autoplay: true,
    autoplaySpeed:1500
});

//------- Deal of the week --------//  
$('.active-review-carusel').owlCarousel({
    items:1,
    loop:true,
    autoplay:true,
    autoplayHoverPause: true,        
    margin:30,
    dots: true
});

 $('.active-pro-dealof-theweek').owlCarousel({
        items: 4,
        loop: true,
        margin:30,
        autoplayHoverPause: false,
        dots: false,
        autoplay: false,
        nav: true,
        navText: ["<span>Back</span>", "<span>Next</span>"],
        responsive: {
            0: {
                items: 1,
            },
            480: {
                items: 1,
            },
            768: {
                items: 2,
            },
            1024: {
                items: 3,
            },
            1280: {
                items: 4,
            },
            1440: {
                items: 4,
            }
        }
    });

$('.active-brand-carusel').owlCarousel({
    items: 4,
    loop: true,
    autoplayHoverPause: true,
    autoplay: true,
    responsive: {
        0: {
            items: 1
        },
        455: {
            items: 2
        },            
        768: {
            items: 3,
        },
        991: {
            items: 4,
        },
        1024: {
            items: 4,
        },
        1920: {
            items: 4,
        }
    }
});

//------- Popular Product , Related Products--------//  
$('.active-review-carusel').owlCarousel({
    items:1,
    loop:true,
    autoplay:true,
    autoplayHoverPause: true,        
    margin:30,
    dots: true
});
$('.active-pro-relerate').owlCarousel({
    items: 4,
    loop: true,
    margin: 30,
    autoplayHoverPause: true,
    dots: false,
    autoplay: false,
    nav: false,
    navText: ["<span class='lnr lnr-arrow-up'></span>", "<span class='lnr lnr-arrow-down'></span>"],
    responsive: {
        0: {
            items: 1,
        },
        480: {
            items: 1,
        },
        768: {
            items: 2,
        },
        1024: {
            items: 3,
        },
        1280: {
            items: 4,
        },
        1440: {
            items: 4,
        }
    }
});
$('.active-brand-carusel').owlCarousel({
    items: 4,
    loop: true,
    autoplayHoverPause: true,
    autoplay: true,
    responsive: {
        0: {
            items: 1
        },
        455: {
            items: 2
        },            
        768: {
            items: 3,
        },
        991: {
            items: 4,
        },
        1024: {
            items: 4,
        },
        1920: {
            items: 4,
        }
    }
});

// FOOTER HOMEPAGE 3
$(document).on('click', '.icon-plus', function (e) {
    var $this = $(this);
    var $thisParent = $this.closest('.t-center-laptop');
    if ($thisParent.length) {
        $thisParent.toggleClass('show-submenu').find('> .f-content-list-item').stop().slideToggle();
    }
    e.preventDefault();
    return false;
});

// PRODUCT DETAIL V3
$('.slider-single').slick({
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false, 
    fade: true,
    asNavFor: '.slider-nav'
  });
  $('.slider-nav').slick({
    slidesToShow: 4,
    slidesToScroll: 1,
    vertical: true,
    arrows: false, 
    verticalSwiping: true,
    asNavFor: '.slider-single',
    dots: false,
    focusOnSelect: true
  });

  // PRODUCT DETAIL V4
$('.slider-single-v4').slick({
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false, 
    fade: true,
    asNavFor: '.slider-nav-v4'
  });
  $('.slider-nav-v4').slick({
    slidesToShow: 4,
    slidesToScroll: 1,
    arrows: false, 
    verticalSwiping: true,
    asNavFor: '.slider-single-v4',
    dots: false,
    focusOnSelect: true
  });

// POPUP QuickView
$('.review').on('click', function(event) {
    $(".product-quick-view").addClass('quick-view-active')
    $(".product-quick-view-overlay").fadeIn();

    // Quick View galary image product
    $('.slide-quickview-single').slick({
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: false, 
        fade: true,
        asNavFor: '.slide-quickview-nav'
    });
    $('.slide-quickview-nav').slick({
        slidesToShow: 3,
        slidesToScroll: 1,
        vertical: true,
        arrows: false, 
        verticalSwiping: true,
        asNavFor: '.slide-quickview-single',
        dots: false,
        focusOnSelect: true
    });
    event.preventDefault()
});
$('.product-quick-view-overlay').on('click', function(event) {
    $(".product-quick-view").removeClass('quick-view-active')
    $(".product-quick-view-overlay").fadeOut()
    event.preventDefault()
});
$('.close-quick-view').on('click', function(event) {
    $(".product-quick-view").removeClass('quick-view-active')
    $(".product-quick-view-overlay").fadeOut()
    event.preventDefault()
});

// POPUP Auto
$('.wishlist').on('click', function(event) {
    $(".popup").addClass('popup-active')
    $(".popup-overlay").fadeIn();
    event.preventDefault()
});
$('.popup-overlay').on('click', function(event) {
    $(".popup").removeClass('popup-active')
    $(".popup-overlay").fadeOut()
    event.preventDefault()
});
$('.close-popup').on('click', function(event) {
    $(".popup").removeClass('popup-active')
    $(".popup-overlay").fadeOut()
    event.preventDefault()
});


// List - Gird Shop Page Fullwidth
$('.list-js').on('click', function(event) {
    $('.box-item-list-shoppage').addClass('item-list-shoppage');
    $('.layout').removeClass("col-xl-3 col-md-6 col-lg-4 col-sm-6 col-12 transition-06").delay(10).queue(function(next){   
        $(this).addClass("col-xl-6 col-lg-6 col-md-6 col-custom-12 col-sm-12 col-12 list-pb");
        next();
    });
});
$('.grid-js').on('click', function(event) {
    $(".box-item-list-shoppage").removeClass('item-list-shoppage');
    $('.layout').removeClass("col-xl-6 col-lg-6 col-md-6 col-custom-12 col-sm-12 col-12 list-pb");
    $('.layout').addClass("col-xl-3 col-md-6 col-lg-4 col-sm-6 col-12 transition-06");
});

// List - Gird Shop Page Fullwidth Sidebar Left
$('.sidebarleft-list-js').on('click', function(event) {
    $('.box-item-list-shoppage').addClass('item-list-shoppage');
    $('.sidebar-left-layout').removeClass("col-xl-4 col-md-6 col-lg-4 col-sm-12 col-12 transition-05").delay(10).queue(function(next){   
        $(this).addClass("col-xl-12 col-lg-12 col-md-12 list-pb");
        next();
    });
});
$('.sidebarleft-grid-js').on('click', function(event) {
    $(".box-item-list-shoppage").removeClass('item-list-shoppage');
    $('.sidebar-left-layout').removeClass("col-xl-12 col-lg-12 col-md-12 list-pb").delay(10).queue(function(next){   
        $(this).addClass("col-xl-4 col-md-6 col-lg-4 col-sm-12 col-12 transition-05");
        next();
    });
});

// SET WIDTH SUBMENU PAGE 1
function setWidthFollowScreenPage1(selectorv1,comparev1,sub1v1,dadv1,sub2v1) {
    if ($(window).width() >= 1025 ) {
        var width=$(comparev1).width()-sub1v1;
        $(selectorv1).css({'width':function(){return width+'px'}})
    }

    $(window).resize(function(){
        if ($(window).width() >= 1025 ) {
        var width=$(comparev1).width()-sub1v1;
        $(selectorv1).css({'width':function(){return width+'px'}})
        }
    })
}
setWidthFollowScreenPage1('.js-dropmenuv1','.js-comparev1','160','.js-dadv1','80');

// SET WIDTH SUBMENU
function setWidthFollowScreen(selector,compare,sub1,dad,sub2) {
    if ($(window).width() >= 1025 ) {
        var width=$(compare).width()-sub1;
        $(selector).css({'width':function(){return width+'px'},'left':function(){
        var left_val=(($(window).width()-width)/2)-$(dad).position().left;
        console.log($(dad).position().left)
        console.log($(window).width())
        console.log(($(window).width()-width)/2)
        return left_val-sub2+'px';
        }})
    }

    $(window).resize(function(){
        if ($(window).width() >= 1025 ) {
        var width=$(compare).width()-sub1;
        $(selector).css({'width':function(){return width+'px'},'left':function(){
            var left_val=-($(dad).position().left-(($(window).width()-width)/2));
            return left_val-sub2+'px';
        }})
        }
    })
}
setWidthFollowScreen('.js-dropmenu','.js-compare','260','.js-dad','80');

// Popup auto show
setTimeout(function(){
    $('.popup').addClass('popup-active');
}, 2000);

//  Range Slider Filter Product Price
$(".js-range-slider").ionRangeSlider({
    type: "double",
    min: 0,
    max: 1000,
    from: 200,
    to: 500,
    grid: false
});

//  Range Slider Filter Product Price
$(".js-range-slider1").ionRangeSlider({
    type: "double",
    min: 0,
    max: 1000,
    from: 200,
    to: 500,
    grid: false
});

// SET HEIGHT BOX BG PAGE-1
function setheightSlide(selector1,compare1) {
    if ($(window).width() >= 1025 ) {
        var height=$(compare1).height();
        height = height + 35;
        $(selector1).css({'height':function(){
            console.log(height)
            return height+'px'
        }})
    }
    if ($(window).width() >= 1280 ) {
        var height=$(compare1).height();
        height = height + 75;
        $(selector1).css({'height':function(){
            console.log(height)
            return height+'px'
        }})
    }
}
setheightSlide('.js-box-bg','.js-box-space');

// BTN SIDEBAR MOBIE
function showSidebar() {
    $('.js-filter').on('click',function(){
      $('.js-fix-sidebar').toggleClass('sidebar-active');
      if ($('.js-fix-sidebar').hasClass('sidebar-active')) {
        $('.js-filter span').removeClass('fa-filter').addClass('fa-times');
      } else {
        $('.js-filter span').removeClass('fa-times').addClass('fa-filter');
      }
    })
}
showSidebar();

// Add active class to the multi layout
var header = document.getElementById("multiLayout");
var btns = header.getElementsByClassName("view-icon");
for (var i = 0; i < btns.length; i++) {
  btns[i].addEventListener("click", function() {
  var current = document.getElementsByClassName("actived");
  console.log(current);
  if (current.length > 0) { 
    current[0].className = current[0].className.replace(" actived", "");
  }
  this.className += " actived";
  });
}

});