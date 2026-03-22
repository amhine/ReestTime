import { Injectable } from '@angular/core';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';
import { rxStompConfig } from '../config/rx-stomp.config';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IMessage } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private rxStomp: RxStomp;

  constructor() {
    this.rxStomp = new RxStomp();
    this.rxStomp.configure(rxStompConfig);
    this.rxStomp.activate();
  }

  watch(topic: string): Observable<any> {
    return this.rxStomp.watch(topic);
  }

  watchUserNotifications(userId: number): Observable<any> {
    const topic = `/topic/notifications/${userId}`;
    console.log(`[STOMP] Abonnement au topic : ${topic}`);
    return this.watch(topic).pipe(
      map(message => JSON.parse(message.body))
    );
  }

  deactivate(): void {
    this.rxStomp.deactivate();
  }
}
