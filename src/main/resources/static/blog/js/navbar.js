// $(document).ready(function () {
//     $('.nav-link:first-child').addClass('active');
//     const navbar = $('.navbar');
//     const scrollThreshold = $(document).height() * 0.1;
//
//     $(window).on('scroll', function () {
//         if ($(this).scrollTop() > scrollThreshold) {
//             navbar.addClass('scrolled');
//             console.log(scrollThreshold);
//         } else {
//             navbar.removeClass('scrolled');
//             console.log(scrollThreshold);
//         }
//     });
//
//     $('.nav-link').on('click', function () {
//         // Remove 'active' class from all nav items
//         $('.nav-link').removeClass('active');
//         // Add 'active' class to the clicked nav item
//         $(this).addClass('active');
//     });
// });

$(document).ready(function () {
    // Add 'active' class to the first nav-link by default

    const navbar = $('.navbar');
    const scrollThreshold = $(document).height() * 0.1;
    console.log(`Scroll threshold: ${scrollThreshold}`); // Log once for debugging

    // Handle scroll event to toggle 'scrolled' class
    $(window).on('scroll', function () {
        if ($(this).scrollTop() > scrollThreshold) {
            navbar.addClass('scrolled');
        } else {
            navbar.removeClass('scrolled');
        }
    });

    const currentPath = window.location.pathname;

    $('.nav-link').each(function () {
        if ($(this).attr('href') === currentPath) {
            $(this).addClass('active');
        } else {
            $(this).removeClass('active');
        }
    });


});




