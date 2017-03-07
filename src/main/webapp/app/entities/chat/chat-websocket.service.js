/**
 * Created by Xavi on 19/01/2017.
 */
angular
    .module('littletimmyApp')
    .factory('ChatTrackerService', ChatTrackerService);

ChatTrackerService.$inject = ['$rootScope', '$window', '$cookies', '$http', '$q', '$localStorage'];

function ChatTrackerService ($rootScope, $window, $cookies, $http, $q, $localStorage) {

    var stompClient = null;
    var subscriber = null;
    var listener = $q.defer();
    var connected = $q.defer();
    var alreadyConnectedOnce = false;

    var service = {
        connect: connect,
        disconnect: disconnect,
        receive: receive,
        sendActivity: sendActivity,
        subscribe: subscribe,
        unsubscribe: unsubscribe
    };

    return service;

    function connect (urlChat, id_chat) {
        //building absolute path so that websocket doesnt fail when deploying with a context path
        var loc = $window.location;
        var url = '//' + loc.host + loc.pathname + urlChat;
        var authToken = angular.fromJson($localStorage.authenticationToken).access_token;
        url += '?access_token=' + authToken;
        /*jshint camelcase: false */
        var socket = new SockJS(url);
        stompClient = Stomp.over(socket);
        //stompClient.debug = null;
        var stateChangeStart;
        var headers = {};
        stompClient.connect(headers, function() {
            connected.resolve('success');
            subscribe(id_chat);
        });
    }

    function disconnect () {
        if (stompClient !== null) {
            stompClient.disconnect();
            stompClient = null;
        }
    }

    function receive () {
        return listener.promise;
    }

    function sendActivity(id_chat, message) {
        if (stompClient !== null && stompClient.connected) {
            stompClient
                .send('/topic/sendMessage/' + id_chat,
                    {},
                    angular.toJson(message));
        }
    }

    function subscribe (id_chat) {
        connected.promise.then(function() {
            subscriber = stompClient.subscribe('/topic/messages/'+id_chat, function(data) {
                listener.notify(angular.fromJson(data.body));
            });
        }, null, null);
    }

    function unsubscribe () {
        if (subscriber !== null) {
            subscriber.unsubscribe();
        }
        listener = $q.defer();
    }
}
