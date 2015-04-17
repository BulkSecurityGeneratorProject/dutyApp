'use strict';

describe('Services Tests ', function () {

    beforeEach(module('dutyappApp'));
    
    beforeEach(function() {
        //Cache everything except rest api requests
        module('ngCacheBuster');
        module(function(httpRequestInterceptorCacheBusterProvider){
            httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*/]);
            httpRequestInterceptorCacheBusterProvider.setLogRequests(true);
        });
    }); 
    
    describe('Auth', function () {
        var $httpBackend, spiedLocalStorageService, authService, spiedAuthServerProvider;       
        //make sure no expectations were missed in your tests.
        //(e.g. expectGET or expectPOST)
        beforeEach(inject(function($injector, localStorageService, Auth, AuthServerProvider) {
            $httpBackend = $injector.get('$httpBackend');
            spiedLocalStorageService = localStorageService;
            authService = Auth;
            spiedAuthServerProvider = AuthServerProvider;     

          }));      
        
        afterEach(function() {
            //$httpBackend.verifyNoOutstandingExpectation();
            //$httpBackend.verifyNoOutstandingRequest();
        });      
        
        it('should call LocalStorageService.clearAll on logout', function(){
            //Given
            $httpBackend.expectGET('i18n/en/global.json').respond(200, '');
            $httpBackend.expectGET('i18n/en/language.json').respond(200, '');
            $httpBackend.expectGET('scripts/components/navbar/navbar.html').respond({});
            $httpBackend.expectGET('i18n/en/global.json').respond(200, '');
            $httpBackend.expectGET('i18n/en/language.json').respond(200, '');
            $httpBackend.expectGET('i18n/en/main.json').respond(200, '');
            $httpBackend.expectGET('scripts/app/main/main.html').respond({}); 
            //Set spy
            spyOn(spiedLocalStorageService, "clearAll").and.callThrough();

            //WHEN
            authService.logout();
            //flush the backend to "execute" the request to do the expectedGET assertion.
            $httpBackend.flush();

            //THEN
            expect(spiedLocalStorageService.clearAll).toHaveBeenCalled();
          });
        
        it('login success', function(){
        /*
            $httpBackend.expectPOST('api/authenticate' , data, {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "Accept": "application/json"
                }).respond({}); 
        */
            $httpBackend.whenPOST('api/authenticate').respond({});  

        
            $httpBackend.expectGET('api/account').respond({
                login:'admin', password:'$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC',
                firstName:'null', lastName:'Administrator', email:'null', activated:'true', langKey:'en', activationKey:'null'
            });  

            //Given
            $httpBackend.expectGET('i18n/en/global.json').respond(200, '');
            $httpBackend.expectGET('i18n/en/language.json').respond(200, '');
            $httpBackend.expectGET('scripts/components/navbar/navbar.html').respond({});
            $httpBackend.expectGET('i18n/en/global.json').respond(200, '');
            $httpBackend.expectGET('i18n/en/language.json').respond(200, '');
            $httpBackend.expectGET('i18n/en/main.json').respond(200, '');
            $httpBackend.expectGET('scripts/app/main/main.html').respond({}); 
        
            var credentials = { 
                username: "admin", 
                password: "$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrc"
            };

            var data = "username=" + credentials.username + "&password="
                    + credentials.password;

            //Set spy
            spyOn(spiedLocalStorageService, "set").and.callThrough();
            
            //WHEN
            //authService.login(credentials, null);
            spiedAuthServerProvider.login(credentials);

            //flush backend
            $httpBackend.flush();

            //THEN
            expect(spiedLocalStorageService.set).toHaveBeenCalled();
        });
    });
});
