(function(){

    angular
        .module('MDC')
        .controller('CustomerController', [
            'customerService', '$routeParams', '$log',
            CustomerController
        ]);


    /**
     * Manages details for a specific customer
     */
    function CustomerController(customerService, $routeParams, $log ) {
        var self = this;

        self.customer        = { };
        self.customerId = $routeParams.customerId;

        customerService
            .get(self.customerId)
            .then( function( customer ) {
                $log.debug("customer " + self.customerId + "'s details loaded");
                self.customer    = customer;
            });

        self.tags = ['Amazon', 'PayPal', '...'];
    }

})();
