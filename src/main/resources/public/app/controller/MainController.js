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
                customers.data.content.forEach(function(c) {
                    c.avatar = "http://www.gravatar.com/avatar/" + CryptoJS.MD5(c.firstName + " " + c.lastName)
                        + "?s=120&d=identicon";
                });
                self.customers    = [].concat(customers.data.content);
            });

        self.showDashboard = function(){
            $state.go('dashboard');
        };

        self.selectUser = function(cust){
            $state.go('user',{customerId : cust.id});
        };
    }



})();
