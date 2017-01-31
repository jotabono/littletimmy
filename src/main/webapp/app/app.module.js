(function() {
    'use strict';

    angular
        .module('littletimmyApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngSanitize',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'ngAnimate',
            'xeditable',
            'ngMaterial'
        ])
        .directive('autolinker', ['$timeout',
            function($timeout) {
                return {
                    restrict: 'A',
                    link: function(scope, element, attrs) {
                        $timeout(function() {
                            var eleHtml = element.html();

                            if (eleHtml === '') {
                                return false;
                            }

                            var text = Autolinker.link(eleHtml, {
                                className: 'autolinker',
                                newWindow: false
                            });

                            element.html(text);

                            var autolinks = element[0].getElementsByClassName('autolinker');

                            for (var i = 0; i < autolinks.length; i++) {
                                angular.element(autolinks[i]).bind('click', function(e) {
                                    var href = e.target.href;
                                    console.log('autolinkClick, href: ' + href);

                                    if (href) {
                                        //window.open(href, '_system');
                                        window.open(href, '_blank');
                                    }

                                    e.preventDefault();
                                    return false;
                                });
                            }
                        }, 0);
                    }
                }
            }
        ])
        .directive('myEnter', function () {
            return function (scope, element, attrs) {
                element.bind("keydown keypress", function (event) {
                    if(event.which === 13) {
                        scope.$apply(function (){
                            scope.$eval(attrs.myEnter);
                        });

                        event.preventDefault();
                    }
                });
            };
        })
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
