import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { ConfigurationService } from "./configuration.service";
import { inject } from "@angular/core";
import { Observable, of } from "rxjs";
import { Configuration } from "./configuration-form/configuration.module";

export const ConfigurationResolver: ResolveFn<any> = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
    configurationService: ConfigurationService = inject(ConfigurationService)): Observable<Configuration> => {

        const configId = route.paramMap.get("configId");

        if (configId) {
            // make api call and get data for current configuration settings
            return configurationService.getConfiguration();
        } else {
            // create and return empty configuration details

            const configuration: Configuration = {
                configId: 0,
                totalTickets: 0,
                ticketReleaseRate: 0,
                customerRetrievalRate: 0,
                maxTicketCapacity: 0
              }

              return of(configuration);
        }
    }