$(document).ready(function () {

    // Check if email exists in localStorage
    const checkEmailExistance = localStorage.getItem('subscribedEmail');

    // Iterate through each like button
    $('.like-button').each(function () {
        const button = $(this); // Current button
        const storyId = button.attr('data-story-id'); // Get the story ID

        if (checkEmailExistance) {
            // Email exists in localStorage, update UI directly
            button.find('.unlike').hide();
            button.find('.liked').show();
        } else {
            // No email in localStorage, check subscription status via AJAX
            $.ajax({
                url: "/api/subscription/status",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ email: checkEmailExistance || "" }),
                success: function (response) {
                    if (response.isSubscribed) {
                        checkLikeStatus(storyId, checkEmailExistance, button);
                    } else {
                        button.find('.unlike').hide();
                        button.find('.liked').show();
                    }
                },
                error: function () {
                    console.error("Failed to check subscription status.");
                    button.find('.unlike').hide();
                    button.find('.liked').show();
                }
            });
        }
    });

    // Check like status for a story
    function checkLikeStatus(storyId, username, button) {
        $.ajax({
            url: `/api/histories/check-like/${storyId}`,
            method: 'POST',
            data: { storyId: storyId, username: username },
            success: function (isLiked) {
                toggleLikeButton(button, isLiked);
            },
            error: function (xhr) {
                handleAjaxError(xhr, 'Checking like status');
            }
        });
    }

    // Fetch and display stories with pagination
    let storiesArray = [];
    let currentPage = 1;
    const itemsPerPage = 5;

    fetchData(currentPage);

    // Search functionality
    $('#search').on('input', function () {
        const searchTerm = $(this).val().toLowerCase();
        const filteredStories = storiesArray.filter(story =>
            story.storyTitle.toLowerCase().includes(searchTerm) ||
            story.historyCategory.toLowerCase().includes(searchTerm)
        );

        filteredStories.length > 0
            ? displayStories(filteredStories)
            : $('.stories-container').html('<p>No data found</p>');
    });

    // Fetch stories data
    function fetchData(page) {
        $.ajax({
            url: `/api/histories?page=${page}&size=${itemsPerPage}`,
            method: 'GET',
            success: function (response) {
                storiesArray = response.content;
                updatePagination(response.totalPages, page);
                displayStories(storiesArray);
            },
            error: function () {
                $('.stories-container').html('<p>Error fetching data</p>');
            }
        });
    }

    // Update pagination UI
    function updatePagination(totalPages, currentPage) {
        const paginationHtml = Array.from({ length: totalPages }, (_, i) => `
            <li class="page-items bg-dark ${i + 1 === currentPage ? 'active' : ''}">
                <a class="page-links" href="#" onclick="changePage(${i + 1})">${i + 1}</a>
            </li>
        `).join('');
        $('#pagination').html(paginationHtml);
    }

    window.changePage = function (page) {
        currentPage = page;
        fetchData(page);
    };

    // Display stories in the container
    window.displayStories = function (stories) {
        if (stories.length === 0) {
            $('.stories-container').html('<p>No data found</p>');
            return;
        }
console.log(stories);
        const storiesHtml = stories.map(story => `
            <div class="stories-list">
                <div class="container-history" style="width: 15rem;">
                    <div class="wrapper">
                        <div class="banner-image">
                            <img src="data:image/png;base64,${story.historyMainImage}" alt="${story.storyTitle}" class="story-photo" loading="lazy">
                        </div>
                        <h3 class="fst-italic lh-1 fs-3 mb-2 welcome-text text-white">
                            ${story.storyTitle?.substring(0,20).concat("...") ?? 'N/A'}
                        </h3>
                        <div class="intro-history paragraph color-letter">
                            <small>${story.historyContent?.substring(0, 30).concat("...") ?? 'N/A'}</small>
                        </div>
                    </div>
                    <div class="details-card">
                        <article class="details-card-text">
                            Category: <span>${story.historyCategory ?? 'N/A'}</span>
                        </article>
                        <article class="details-card-text">
                            on: <span>${story.historyDate ? new Date(story.historyDate).toLocaleDateString() : 'Unknown date'}</span>
                        </article>
                    </div>
                    <div class="button-wrapper">
                        <a href="/api/histories/view-entire-story/${story.historyId ?? '#'}" class="btn-voir-plus outline">Voir plus</a>
                        <button class="fill like-button d-none" type="button" data-story-id="${story.historyId}">
                            <span class="unlike">
                                <ion-icon name="heart-outline"></ion-icon>
                            </span>
                        </button>
                        <ion-icon  class="reader" style="color:#F6F7FB; font-size: 1rem;" name="reader"></ion-icon>
                        <span class="numbering" id="like-count-${story.historyId}">${story.likesCount ?? 0} Reads</span>
                    </div>
                </div>
            </div>
        `).join('');
        $('.stories-container').html(storiesHtml);
    }

    // Handle like button clicks
    $('.stories-container').on('click', '.like-button', function () {
        const button = $(this);
        const storyId = button.data('story-id');
        const likeCountSpan = $('#like-count-' + storyId);
        const username = localStorage.getItem('subscribedEmail');

        if (!username) {
            Swal.fire({
                icon: 'warning',
                title: 'Unknown user',
                html: '<b>Please!</b> Would you like to subscribe to like and comment?',
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
                confirmButtonColor: '#3a6873',
                cancelButtonColor: '#a65252',
                background: '#f7f9fc',
                showCancelButton: true,
                timer: 18000,
                preConfirm: () => window.location.href = "/",
            });
            return;
        }

        $.ajax({
            url: `/api/histories/like/${storyId}`,
            method: 'POST',
            data: { storyId, username },
            success: function (updatedLikes) {
                likeCountSpan.text(`${updatedLikes.likeCount} likes`);
                toggleLikeButton(button, updatedLikes.hasLiked);
            },
            error: function (xhr) {
                handleAjaxError(xhr, 'Liking the story');
            }
        });
    });

    function toggleLikeButton(button, isLiked) {
        if (isLiked) {
            button.find('.unlike').hide();
            button.find('.liked').show();
        } else {
            button.find('.unlike').show();
            button.find('.liked').hide();
        }
    }

    function handleAjaxError(xhr, action) {
        if (xhr.status === 400) {
            console.error(`Invalid request during ${action}:`, xhr.responseText);
            alert('There was an issue with your request. Please try again.');
        } else if (xhr.status === 404) {
            console.error(`Story not found during ${action}.`);
            alert('The story you are trying to like does not exist.');
        } else {
            console.error(`Unexpected error during ${action}.`);
            alert('Something went wrong. Please try again later.');
        }
    }

});
