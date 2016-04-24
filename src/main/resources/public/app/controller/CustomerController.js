(function(){

    angular
        .module('MDC')
        .controller('CustomerController', [
            'customerService', 'commonService', '$stateParams', '$log',
            CustomerController
        ]);


    /**
     * Manages details for a specific customer
     */
    function CustomerController(customerService, commonService, $stateParams, $log ) {
        var self = this;

        self.customer        = { };
        self.customerId = $stateParams.customerId;

        var f = commonService.from.toISOString().slice(0,10);
        var t = commonService.till.toISOString().slice(0,10);

        self.tags = ['Amazon', 'PayPal', '...'];

        self.companies = [{
            name: "Amazon",
            icon: "./assets/amazon.png"
        },{
            name: "PayPal",
            icon: "./assets/paypal.png"
        }];

        self.connections = [{}];
        self.planned = [{}];

        customerService
            .get(self.customerId, f, t)
            .then( function( customers ) {
                var customer = customers.length > 0 ? customers[0] : {};
                $log.debug("customer " + self.customerId + "'s details loaded");
                customer.data.avatar = "http://www.gravatar.com/avatar/" + CryptoJS.MD5(customer.data.firstName + " "
                        + customer.data.lastName) + "?s=120&d=identicon";
                self.tags = customer.classifications || self.tags;
                self.companies = customer.companies || self.companies;
                self.customer    = customer.data;
            });

        customerService
            .getCompanies(self.customerId)
            .then(function(comps){
                self.companies = comps || self.companies;
            });

    }

})();
