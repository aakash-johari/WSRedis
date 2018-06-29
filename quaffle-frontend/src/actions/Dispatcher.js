import Flux from 'flux';
import Queue from 'sync-queue';

let queue = new Queue();
queue['id'] = 'Dispatcher';

let Dispatcher = Object.assign(new Flux.Dispatcher(), {
  handleAction (eventData) {

    let payload = {};

    payload = 
      {
        type: eventData.type,
        data: eventData.data
      };

    queue.place(() => {

      this.dispatch(payload);
      queue.next();
    }); 
  }
});

export default Dispatcher;