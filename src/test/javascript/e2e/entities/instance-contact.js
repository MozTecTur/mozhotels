'use strict';

describe('InstanceContact e2e test', function () {

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));

    beforeAll(function () {
        browser.get('/');
        browser.driver.wait(protractor.until.elementIsVisible(element(by.css('h1'))));

        accountMenu.click();
        login.click();

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should load InstanceContacts', function () {
        entityMenu.click();
        element(by.css('[ui-sref="instance-contact"]')).click().then(function() {
            expect(element.all(by.css('h2')).first().getText()).toMatch(/InstanceContacts/);
        });
    });

    it('should load create InstanceContact dialog', function () {
        element(by.css('[ui-sref="instance-contact.new"]')).click().then(function() {
            expect(element(by.css('h4.modal-title')).getText()).toMatch(/Create or edit a InstanceContact/);
            element(by.css('button.close')).click();
        });
    });

    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
