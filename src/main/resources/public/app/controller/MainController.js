(function(){

    angular
        .module('MDC')
        .controller('MainController', [
            'mainService', '$log', '$state',
            MainController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function MainController(mainService, $log, $state ) {
        var self = this;

        self.customers        = [ ];

        // Load all registered users

        mainService
            .loadAllCustomers()
            .then( function( customers ) {
                self.customers    = [].concat(customers);
            });

        self.showDashboard = function(){
            $state.go('dashboard');
        };

        self.selectUser = function(){
            $state.go('user');
        };
    }



})();