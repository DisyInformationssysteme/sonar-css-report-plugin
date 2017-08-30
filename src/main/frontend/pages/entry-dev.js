window.registerExtension('cssreport/index', function (options) {
    var script = document.createElement('script');
    var cleanUp;
    script.onload = function() {
        cleanUp = CssReport.default(options);
    };
    script.src = 'http://localhost:8080/build/bundle.js';
    document.head.appendChild(script);

    return function() {
        if (cleanUp) {
            cleanUp();
        }
    };
});
