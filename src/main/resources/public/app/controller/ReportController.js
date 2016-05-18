(function(){

    angular
        .module('MDC')
        .controller('ReportController', [
            'commonService', '$log', '$state',
            ReportController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function ReportController(commonService, $log, $state ) {
        var self = this;

        self.statistics        = { };
        self.common = commonService;

        self.customers = self.customers || [{
                name : "Test McTestington",
                customerId : 123
            },{
                name : "Gabriel McKormick",
                customerId : 222
            }];

    }



})();
