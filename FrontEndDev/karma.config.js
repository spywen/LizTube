// Karma configuration
// Generated on Sun Nov 02 2014 15:21:54 GMT+0100 (CET)

module.exports = function (config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '',


        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine'],


        // list of files / patterns to load in the browser
        files: [
            'bower_components/angular/angular.js',
            'bower_components/lodash/dist/lodash.js',
            'bower_components/angular-material/angular-material.js',
            'bower_components/angular-mocks/angular-mocks.js',
            'bower_components/angular-route/angular-route.js',
            'bower_components/restangular/dist/restangular.js',
            'bower_components/angular-messages/angular-messages.js',
            'bower_components/ng-file-upload/angular-file-upload-shim.js',
            'bower_components/ng-file-upload/angular-file-upload-all.js',
            'bower_components/angular-sanitize/angular-sanitize.js',
            'bower_components/videogular/videogular.js',
            'bower_components/videogular-controls/vg-controls.js',
            'bower_components/videogular-overlay-play/vg-overlay-play.js',
            'bower_components/videogular-poster/vg-poster.js',
            'bower_components/ng-clip/dest/ng-clip.min.js',
            'bower_components/zeroclipboard/dist/ZeroClipboard.js',
            'resources/**/*.js',
            'resources/**/*.spec.js'
        ],


        // list of files to exclude
        exclude: [
            'resources/common/themes/**/*',
            'resources/modules/test/**/*'
        ],


        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        preprocessors: {
            'resources/**/!(*.spec|partials|app).js': 'coverage'
        },

        coverageReporter: { // name => coverage
            reporters: [
                { type: 'html', subdir: 'report-html'}
                //{ type: 'teamcity', subdir: '.', file: 'teamcity.txt' }
            ]
        },

        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        reporters: ['progress', 'coverage'],

        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['PhantomJS'],


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false
    });
};