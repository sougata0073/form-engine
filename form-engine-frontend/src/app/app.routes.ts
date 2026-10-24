import { Routes } from '@angular/router';
import { loggedInGuard } from './guard/logged-in-guard';

export const routes: Routes = [
  {
    path: 'forms',
    canActivate: [loggedInGuard],
    canActivateChild: [loggedInGuard],
    children: [
      {
        path: ':formId',
        children: [
          {
            path: 'edit',
            loadComponent: () => import('./edit-form/edit-form').then((m) => m.EditForm),
            children: [
              {
                path: 'questions',
                loadComponent: () =>
                  import('./edit-form/edit-form-questions/edit-form-questions').then(
                    (m) => m.EditFormQuestions,
                  ),
              },
              {
                path: 'responses',
                loadComponent: () =>
                  import('./edit-form/edit-form-responses/edit-form-responses').then(
                    (m) => m.EditFormResponses,
                  ),
                children: [
                  {
                    path: 'summary',
                    loadComponent: () =>
                      import('./edit-form/edit-form-responses/edit-form-responses-summary/edit-form-responses-summary').then(
                        (m) => m.EditFormResponsesSummary,
                      ),
                  },
                  {
                    path: 'question',
                    loadComponent: () =>
                      import('./edit-form/edit-form-responses/edit-form-responses-question/edit-form-responses-question').then(
                        (m) => m.EditFormResponsesQuestion,
                      ),
                  },
                  {
                    path: 'individual',
                    loadComponent: () =>
                      import('./edit-form/edit-form-responses/edit-form-responses-individual/edit-form-responses-individual').then(
                        (m) => m.EditFormResponsesIndividual,
                      ),
                  },
                ],
              },
              {
                path: 'settings',
                loadComponent: () =>
                  import('./edit-form/edit-form-settings/edit-form-settings').then(
                    (m) => m.EditFormSettings,
                  ),
              },
            ],
          },
          {
            path: 'view',
            loadComponent: () => import('./view-form/view-form').then((m) => m.ViewForm),
          },
          {
            path: 'preview',
            loadComponent: () => import('./preview-form/preview-form').then((m) => m.PreviewForm),
          },
        ],
      },
    ],
  },
  {
    path: 'register',
    loadComponent: () => import('./auth/register/register').then((m) => m.Register),
  },
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login').then((m) => m.Login),
  },
  {
    path: 'social-auth-success',
    loadComponent: () =>
      import('./auth/social-auth-success/social-auth-success').then((m) => m.SocialAuthSuccess),
  },
  {
    path: 'home',
    loadComponent: () => import('./home/home').then((m) => m.Home),
    canActivate: [loggedInGuard],
  },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
];
