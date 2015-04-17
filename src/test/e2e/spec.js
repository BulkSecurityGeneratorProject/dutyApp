// spec.js
describe('Protractor Demo App', function() {

  var AngularHomepage = function() {
    this.loginBtn = element(by.id('loginBtn'));
    this.username =  element(by.model('username'));
    this.password =  element(by.model('password'));

    this.modal = element(by.id('saveEscalationPolicyModal'));
    this.createPolicyBtn = element(by.id('createPolicyBtn'));
    this.savePolicyBtn = element(by.id('savePolicyBtn'));
    this.policyname = element(by.model('escalationPolicy.policy_name'));

    this.policies = element.all(by.binding('escalationPolicy.id'));


    this.get = function(url) {
      browser.get(url);
    };

    this.setKeys = function(attr, key) {
      this[attr].sendKeys(key);
    };

    this.click = function(btnId) {
        this[btnId].click();
    };
  };

  beforeEach(function() {
    //browser.get('http://juliemr.github.io/protractor-demo/');
	  //browser.get('http://localhost:8080/#/escalationPolicy');

  });

  it('test create escalationPolicy process', function() {

  	// implicit and page load timeouts
    browser.manage().timeouts().pageLoadTimeout(40000);
    browser.manage().timeouts().implicitlyWait(25000);


    var angularHomepage = new AngularHomepage();
    angularHomepage.get('http://127.0.0.1:8080/#/login');

    expect(browser.getTitle()).toEqual('Authentication');


    // first it should go to login
    angularHomepage.setKeys('username', 'admin');
    angularHomepage.setKeys('password', 'admin');
    angularHomepage.click('loginBtn');
    browser.waitForAngular();

    // successful login direct to HOME
    // now GET escalationPolicy page again
    angularHomepage.get('http://127.0.0.1:8080/#/escalationPolicy');
	  expect(angularHomepage.modal.isDisplayed()).toEqual(false);

    angularHomepage.click('createPolicyBtn');
    browser.waitForAngular();

    //TO-DO: don't know why fail to expect here
    //expect(angularHomepage.modal.isDisplayed()).toEqual(true);
    angularHomepage.setKeys ('policyname', 'Policy Test 1');
    expect(angularHomepage.modal.isDisplayed()).toEqual(true);
    angularHomepage.click('savePolicyBtn');

    browser.waitForAngular();
    angularHomepage.policies.then(function(items) { 
      expect(items.length).toBe(3);
    });
  });
});