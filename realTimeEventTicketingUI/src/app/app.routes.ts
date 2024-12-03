import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ControlPanelComponent } from './control-panel/control-panel.component';
import { TicketStatusComponent } from './ticket-status/ticket-status.component';
import { LogDisplayComponent } from './log-display/log-display.component';

export const routes: Routes = [
    {path: "", component: HomeComponent},
    {path: "config-settings", component: ControlPanelComponent},
    {path: "ticketpool", component: TicketStatusComponent},
    {path: "logs", component: LogDisplayComponent},
    {path: "ticket-sales", component: TicketStatusComponent},
    {path: "**", component:PageNotFoundComponent}
];
