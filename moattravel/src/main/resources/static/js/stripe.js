const stripe = Stripe('pk_test_51RTcFKIOCMczqzs8vTq4qnqMrUvCZy2jo51i0CXEk5YPbwCvnYwlWfT1LHrar2vqKaY5vvTN4OXNLnzxV7VCYbY900cEKBcEPP');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});