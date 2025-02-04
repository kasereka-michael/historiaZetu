$(document).ready(function(){

    $('.btn-voir-plus').on('click', function (e) {
        e.preventDefault();

        const storyId = $(this).data('story-id'); // Get the story ID
        const currentPage = 1; // Set the starting page
        // fetchData(currentPage, storyId); // Call fetchData with the story ID
    });


    let storiesArray = [];  // Stores the stories fetched from the backend
    const itemsPerPage = 5;  // Number of stories per page

    // Fetch data from backend for the first time
    // fetchData(currentPage);

    function fetchData(page, storyId) {
            $.ajax({
                url: `/api/histories/view?page=${page}&size=${itemsPerPage}&storyId=${storyId}`,
                method: 'GET',
                success: function(response) {
                    // Assuming the backend returns a paginated response
                    storiesArray = response.content;
                    console.log("storiesArray :: ", storiesArray);

                    updatePagination(response.totalPages, page); // Update pagination controls
                    displayStories(storiesArray); // Render the fetched stories
                },
                error: function() {
                    $('.carousel-container').html('<p>Error fetching data</p>');
                }
            });
        }

    // Update pagination based on the number of pages
    function updatePagination(totalPages, currentPage) {
        let paginationHtml = '';

        for (let i = 1; i <= totalPages; i++) {
            paginationHtml += `
                <li class="page-items bg-dark ${i === currentPage ? 'active' : ''}">
                    <a class="page-links" href="#" onclick="changePage(${i})">${i}</a>
                </li>
            `;
        }

        $('#pagination').html(paginationHtml);
    }





    function displayStories(currentIndex) {
        let storiesHtml = '';

        if (!stories || stories.length === 0) {
            $('.card-continers').html('<p>No data found</p>');
            return;
        }

        // Ensure currentIndex is valid
        if (currentIndex < 0 || currentIndex >= stories.length) {
            console.error('Invalid index:', currentIndex);
            $('.card-continers').html('<p>Invalid story index</p>');
            return;
        }

        var story = stories[currentIndex];

        storiesHtml += `
        <div class="col-12 col-lg-6 order-2 order-lg-1">
            <div class="main-history main-history-small" style="padding: 5px; backdrop-filter: blur(16px) saturate(180%);
                -webkit-backdrop-filter: blur(16px) saturate(180%); background-color: rgba(22, 22, 22, 0.25);
                border-radius: 10px; overflow: hidden; border: 1px solid #777;">

                <div class="story-header">
                    <h1 class="story-title fst-italic lh-1 fs-2 mb-2">${story.storyTitle ?? 'Untitled Story'}</h1>
                    <div class="story-info">
                        <p>Photo by: <strong>${story.storyPhotoGrapherName ?? 'N/A'}</strong></p>
                        <p>Story teller: <strong>${story.storyAuthor ?? 'N/A'}</strong></p>
                        <p>Posted Date: <strong>${story.historyDate ? new Date(story.historyDate).toLocaleDateString() : 'Unknown date'}</strong></p>
                    </div>
                </div>

                <div class="share-container">
                    <p>Partager</p>
                    <a href="#" title="Copy the story"><ion-icon class="copy" name="copy"></ion-icon></a>
                    <a href="https://www.facebook.com/sharer/sharer.php?u=${story.storyFacebookLink ?? '#'}" target="_blank">
                        <ion-icon class="share-facebook" name="logo-facebook"></ion-icon>
                    </a>
                    <a href="https://www.instagram.com/?url=${story.storyInstagramLink ?? '#'}" target="_blank">
                        <ion-icon class="share-instagram" name="logo-instagram"></ion-icon>
                    </a>
                    <a href="https://api.whatsapp.com/send?text=${story.storyWhatsAppLink ?? '#'}" target="_blank">
                        <ion-icon class="share-whatsapp" name="logo-whatsapp"></ion-icon>
                    </a>
                </div>

                <img src="data:image/png;base64,${story.historyMainImage}" alt="${story.storyTitle}" class="story-photo">

                <div class="story-caption">
                    ${story.storyCaption ?? 'N/A'}
                    <a href="/api/histories/view-story/${story.historyId}" class="btn-voir-plus outline">Lire l'histoire entière...</a>
                </div>

                <div class="story-content">
                    <p style="color: #fff; text-align:left">${story.historyContent.substring(0, 1000).concat("...") ?? 'N/A'}</p>
                </div>

                <div class="story-stats d-none">
                    <span><ion-icon name="reader"></ion-icon> Readers: ${story.readersCount ?? 0}</span>
                    <span><ion-icon name="share-social"></ion-icon> Shared: ${story.sharesCount ?? 0}</span>
                    <span><ion-icon name="chatbubbles"></ion-icon> Comments: ${story.commentsCount ?? 0}</span>
                </div>

                <div class="comment-box">
                    <textarea placeholder="Leave a comment..."></textarea>
                    <button type="button" class="btn-primary">Post Comment</button>
                </div>
                <div class="comments-list">
                    ${(story.comments || []).map(comment => `
                        <div class="comment">
                            <p class="comment-author">${comment.author ?? 'Anonymous'}</p>
                            <p class="comment-text">${comment.text}</p>
                            <p class="comment-date">Posted on: ${comment.date ?? 'Unknown'}</p>
                        </div>
                    `).join('')}
                </div>
            </div>
        </div>
    `;

        // Handling Related Stories
        if (stories.length > currentIndex + 1) {
            storiesHtml += `
        <div class="col-12 col-lg-6 order-1 order-lg-2 d-flex">
            <div class="d-flex flex-column w-100">
                <div class="carousel slide flex-fill" data-bs-ride="carousel">
                    <div class="carousel-inner">
                        <div class="carousel-container">
                            <div class="stories-list">
        `;

            for (let i = currentIndex + 1; i < stories.length; i++) {
                let nextStory = stories[i];
                storiesHtml += `
                <div class="container-history" style="width: 15rem;">
                    <div class="wrapper">
                        <div class="banner-image">
                            <img src="data:image/png;base64,${nextStory.historyMainImage}" alt="${nextStory.storyTitle}" class="story-photo">
                        </div>
                        <h3 class="fst-italic lh-1 fs-5 mb-2 welcome- text-white">${nextStory.storyTitle?.substring(0,10).concat("...") ?? 'N/A'}</h3>
                        <div class="intro-history paragraph color-letter"><small>${nextStory.historyContent?.substring(0, 30).concat("...") ?? 'N/A'}</small></div>
                    </div>
                    <div class="details-card">
                        <article class="details-card-text">Category: <span>${nextStory.historyCategory ?? 'N/A'}</span></article>
                        <article class="details-card-text">on: <span>${nextStory.historyDate ? new Date(nextStory.historyDate).toLocaleDateString() : 'Unknown date'}</span></article>
                    </div>
                    <div class="button-wrapper">
                         <a href="/api/histories/view-story/${nextStory.historyId ?? '#'}" class="btn-voir-plus outline">Voir plus</a>
                        <button class="fill like-button d-none" type="button" data-story-id="${nextStory.historyId}">
                            <span class="unlike">
                                <ion-icon name="heart-outline"></ion-icon>
                            </span>
                        </button>
                        <ion-icon name="reader"></ion-icon>
                        <span class="numbering" id="like-count-${nextStory.historyId}">${nextStory.likesCount ?? 0} Reads</span>
                    </div>
                </div>
            `;
            }

            storiesHtml += `
                            </div>
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#relatedCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#relatedCarousel" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    </button>
                </div>
            </div>
        </div>
        `;
        }

        $('.card-continers').html(storiesHtml);
    }



    function rotateStoriesLeft() {
        if (stories.length > 1) {
            const firstStory = stories.shift(); // Remove the first story
            stories.push(firstStory); // Add it to the end
            displayStories(stories); // Update the display
        }
    }

    function rotateStoriesRight() {
        if (stories.length > 1) {
            const lastStory = stories.pop(); // Remove the last story
            stories.unshift(lastStory); // Add it to the beginning
            displayStories(stories); // Update the display
        }
    }

    // Attach event listeners to carousel controls
    $('.carousel-control-prev').on('click', function () {
        rotateStoriesLeft();
    });

    $('.carousel-control-next').on('click', function () {
        rotateStoriesRight();
    });




});