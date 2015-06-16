<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html data-ng-app="liztube">
<head>
    <title data-ng-bind="title"></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content ="">
    <meta name="keywords" content="">
    <meta name="Author" content="LizTube" >
    <meta name="Robots" content="all" >
    <meta name="Rating" content="general" >
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/app/dist/img/favicon.png" />

    <!-- inject:css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/dist/libs/font-awesome/css/font-awesome.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/dist/libs/angular-material/angular-material.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/dist/libs/videogular-themes-default/videogular.css">
    <!-- endinject -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/dist/css/all.css">
    <base href="/"/>
</head>
<body ng-controller="mainCtrl">
    <header></header>
    <div data-ng-view></div>
    <!-- inject:js -->
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular/angular.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular-animate/angular-animate.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular-aria/angular-aria.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular-route/angular-route.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/ng-file-upload/angular-file-upload-shim.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/ng-file-upload/angular-file-upload.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/lodash/dist/lodash.compat.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular-messages/angular-messages.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular-mocks/angular-mocks.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angularjs-gravatar/dist/angularjs-gravatardirective.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular-sanitize/angular-sanitize.min.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/jquery/dist/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/SHA-1/sha1.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/zeroclipboard/dist/ZeroClipboard.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/ng-clip/dest/ng-clip.min.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angular-material/angular-material.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/restangular/dist/restangular.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/videogular/videogular.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/videogular-controls/vg-controls.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/videogular-buffering/vg-buffering.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/videogular-overlay-play/vg-overlay-play.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/videogular-poster/vg-poster.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/videogular-ima-ads/vg-ima-ads.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/waypoints/waypoints.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-adobe.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-chartbeat.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-cnzz.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-flurry.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-ga-cordova.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-ga.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-gtm.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-kissmetrics.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-mixpanel.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-piwik.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-scroll.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-segmentio.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-splunk.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-woopra.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-marketo.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/angulartics/src/angulartics-intercom.js"></script>
    <script src="${pageContext.request.contextPath}/app/dist/libs/videogular-angulartics/vg-analytics.js"></script>
    <!-- endinject -->
    <script>
        window.user = ${userConnected};
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/app/dist/js/all.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/app/dist/partials/partials.js"></script>
</body>
</html>