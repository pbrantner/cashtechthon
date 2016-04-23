(function(){
    'use strict';

    angular.module('MDC');

    angular.module('MDC')
        .service('dashboardService', ['$q', DashboardService]);

    /**
     * Dashboard Data Service
     *
     * @returns {{loadStatistics: Function}}
     */
    function DashboardService($q) {
        return {
            loadStatistics : function() {
                var statistics = { "name": "data" };
                //var users = $http.get('/someUrl');

                return $q.when(statistics);
            }
        };
    }

})();
