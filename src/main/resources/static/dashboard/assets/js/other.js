const searchBtn = document.getElementById('seach-btn');
const searchContainer = document.getElementById('search-container');

searchBtn.addEventListener('click', function () {
    console.log("yes i have been clicked");
    searchContainer.removeClassName = 'd-none';

});


    document.addEventListener('DOMContentLoaded', function() {
    var p = document.getElementById('introText');
    var text = p.innerText || p.textContent;
    var lines = text.split('\n');

    // Limit to 4 lines
    if (lines.length > 4) {
    p.innerHTML = lines.slice(0, 4).join('\n') + '...'; // Adding ellipsis at the end
}
});

// When the DOM is ready, run this function
$(document).ready(function() {
    "use strict";
    //Set the carousel options
    $('#quote-carousel').carousel({
        pause: true,
        interval: 4000,
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: false,
        smartSpeed: 1000,
        center: true,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsive: {
            0:{
                items:1
            },
            768:{
                items:2
            }
        }
    });



})(jQuery);
