/**
 * Created by Youcef on 01/03/2015.
 */
angular.module("liztube.themes",[
    "ngMaterial"
]).config(function ($mdThemingProvider){
    //Theme material design.
    $mdThemingProvider.definePalette('liztubePalette', {
        '50': 'ffebee',
        '100': 'ffcdd2',
        '200': 'ef9a9a',
        '300': 'e57373',
        '400': 'ef5350',
        '500': '333333',
        '600': 'e53935',
        '700': 'd32f2f',
        '800': 'c62828',
        '900': 'b71c1c',
        'A100': 'ff8a80',
        'A200': 'ff5252',
        'A400': 'ff1744',
        'A700': 'd50000',
        'contrastDefaultColor': 'dark',   // whether, by default, text (contrast)
        // on this palette should be dark or light
        'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
            '200', '300', '400', 'A100'],
        'contrastLightColors': 'light'    // could also specify this if default was 'dark'
    });
    $mdThemingProvider.theme('default')
        .primaryPalette('liztubePalette')
        .accentPalette('blue',{
            'default': '700'
        }).warnPalette("red",{
            'default': '800'
        });
    $mdThemingProvider.theme('navbar')
        .primaryPalette('grey', {
            'default': '200'
        }).accentPalette('grey', {
            'default': '900'
        }).warnPalette("red",{
            'default': '800'
        });
    $mdThemingProvider.theme('sub-bar')
        .primaryPalette('grey', {
            'default': '600'
        }).accentPalette('blue-grey', {
            'default': '500'
        }).warnPalette("red",{
            'default': '800'
        });
    $mdThemingProvider.setDefaultTheme('default');
});
