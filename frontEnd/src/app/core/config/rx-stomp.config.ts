import { RxStompConfig } from '@stomp/rx-stomp';
import { environment } from '../../../environments/environment';

export const rxStompConfig: RxStompConfig = {
  brokerURL: 'ws://localhost:8083/ws',

  connectHeaders: {
  },

  heartbeatIncoming: 0,
  heartbeatOutgoing: 20000,
  reconnectDelay: 500,

  debug: (msg: string): void => {
    console.log('[STOMP Debug]', new Date(), msg);
  },

  beforeConnect: () => {
    console.log('[STOMP] Tentative de connexion...');
  }
};
