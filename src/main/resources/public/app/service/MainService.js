(function(){
    'use strict';

    angular.module('MDC',[]);

    angular.module('MDC')
        .service('mainService', ['$q', MainService]);

    /**
     * Users DataService
     * Uses embedded, hard-coded data model; acts asynchronously to simulate
     * remote data service call(s).
     *
     * @returns {{loadAll: Function}}
     * @constructor
     */
    function MainService($q) {
        return {
            loadAllUsers : function() {
                var users = [{"name": "User1"}, {"name":"User2"}];
                //var users = $http.get('/someUrl');

                return $q.when(users);
            }
        };
    }

})();
