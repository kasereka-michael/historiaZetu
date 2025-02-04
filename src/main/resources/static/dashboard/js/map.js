am5.ready(function() {

    // Create root element
    var root = am5.Root.new("visitfromworld");

    // Set themes
    root.setThemes([
        am5themes_Animated.new(root)
    ]);

    // Create the map chart
    var chart = root.container.children.push(am5map.MapChart.new(root, {
        panX: "translateX",
        panY: "translateY",
        projection: am5map.geoMercator()
    }));

    // Create main polygon series for countries
    var polygonSeries = chart.series.push(am5map.MapPolygonSeries.new(root, {
        geoJSON: am5geodata_worldLow,
        exclude: ["AQ"]
    }));

    polygonSeries.mapPolygons.template.setAll({
        tooltipText: "{name}",
        toggleKey: "active",
        interactive: true
    });

    // Define the countries and their colors
    var countryColors = {
        "Rwanda": am5.color(0x26C281),  // Example color for Rwanda (28%)
        "Democratic Republic of the Congo": am5.color(0x3ec5d6),  // Example color for DRC (21%)
        "Ethiopia": am5.color(0xBF55EC),  // Example color for Ethiopia (18%)
        "Ghana": am5.color(0xFF0000)  // Example color for Ghana (12%)
    };

    polygonSeries.mapPolygons.template.adapters.add("fill", function(fill, target) {
        var countryName = target.dataItem.dataContext.name;
        if (countryColors[countryName]) {
            return countryColors[countryName];
        }
        return fill;
    });

    // Hover state
    polygonSeries.mapPolygons.template.states.create("hover", {
        fill: root.interfaceColors.get("primaryButtonHover")
    });

    // Active state
    polygonSeries.mapPolygons.template.states.create("active", {
        fill: root.interfaceColors.get("primaryButtonHover")
    });

    // Add zoom control
    var zoomControl = chart.set("zoomControl", am5map.ZoomControl.new(root, {}));
    zoomControl.homeButton.set("visible", true);

    // Set clicking on "water" to zoom out
    chart.chartContainer.get("background").events.on("click", function () {
        chart.goHome();
    });

    // Make stuff animate on load
    chart.appear(1000, 100);

});


$('#user-rec-btn').click(function(e) {

        e.preventDefault();
        const confirmPassword = $('#exampleInputConfirmPassword1').val().trim()
        // Collect form data and trim whitespace
        var formData = {
            firstName: $('#exampleInputUsername1').val().trim(),
            lastName: $('#lastname').val().trim(),
            userEmail: $('#exampleInputEmail1').val().trim(),
            phoneNumber: $('#phonenumber').val().trim(),
            userPassword: $('#exampleInputPassword1').val().trim(),
            roles: {
                roleId: $('#roleSelect').val()
            }
        };

        // Validate form data
        if (!formData.firstName) {
            alert("First name is required");
            return;
        }

        if (!formData.lastName) {
            alert("Last name is required");
            return;
        }

        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!formData.userEmail || !emailPattern.test(formData.userEmail)) {
            alert("Please enter a valid email address");
            return;
        }

        var phonePattern = /^[0-9]{10}$/; // Assuming phone number should be 10 digits
        if (!formData.phoneNumber || !phonePattern.test(formData.phoneNumber)) {
            alert("Please enter a valid phone number");
            return;
        }

        if (!formData.userPassword || formData.userPassword.length < 6) {
            alert("Password must be at least 6 characters long");
            return;
        }

        if (formData.userPassword !== confirmPassword) {
            alert("Passwords do not match");
            return;
        }

        // Send AJAX request if all validations pass
        $.ajax({
            type: 'POST',
            url: '/api/v1/users/registration', // Change this to your actual endpoint URL
            data: JSON.stringify(formData),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(response) {
                $('#userRegistrationForm')[0].reset();
                alert('User created successfully');
            },
            error: function(xhr, status, error) {
                // Handle error
                console.error('Error: ' + error);
            }
        });
    });
