/**
 * This implementation of the {@link model.IStoreModel} implements the customer traffic as
 * three independent models indicating the various components of the managers interest.  The
 * {@link model.originalModel.ICustomerArrivalModel} represents the rate at which customers
 * enter the store and ouputs the time of arrival.  The
 * {@link model.originalModel.ICustomerMixModel} determines what type of customer. While the
 * {@link model.originalModel.TimeInStoreModel} indicates how long the customer staid in the store.
 * This approach is warranted as the specific types of customers lacked the details and this
 * accomplished the result of modelling the store traffic.
 */
package model.originalModel;