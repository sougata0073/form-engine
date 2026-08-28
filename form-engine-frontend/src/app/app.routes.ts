import {Routes} from '@angular/router';
import {EditForm} from './edit-form/edit-form';
import {EditFormQuestions} from './edit-form/edit-form-questions/edit-form-questions';
import {EditFormResponses} from './edit-form/edit-form-responses/edit-form-responses';
import {EditFormSettings} from './edit-form/edit-form-settings/edit-form-settings';
import {ViewForm} from './view-form/view-form';
import {PreviewForm} from './preview-form/preview-form';
import {Register} from './auth/register/register';
import {SocialAuthSuccess} from './auth/social-auth-success/social-auth-success';
import {Home} from './home/home';
import {
  EditFormResponsesSummary
} from './edit-form/edit-form-responses/edit-form-responses-summary/edit-form-responses-summary';
import {
  EditFormResponsesQuestion
} from './edit-form/edit-form-responses/edit-form-responses-question/edit-form-responses-question';
import {
  EditFormResponsesIndividual
} from './edit-form/edit-form-responses/edit-form-responses-individual/edit-form-responses-individual';
import {loggedInGuard} from './guard/logged-in-guard';
import {Login} from './auth/login/login';
import {
  AllResponsesSummary
} from './edit-form/edit-form-responses/edit-form-responses-summary/all-responses-summary/all-responses-summary';

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
            component: EditForm,
            children: [
              {
                path: 'questions',
                component: EditFormQuestions
              },
              {
                path: 'responses',
                component: EditFormResponses,
                children: [
                  {
                    path: 'summary',
                    component: EditFormResponsesSummary
                  },
                  {
                    path: 'question',
                    component: EditFormResponsesQuestion
                  },
                  {
                    path: 'individual',
                    component: EditFormResponsesIndividual
                  }
                ]
              },
              {
                path: 'settings',
                component: EditFormSettings
              }
            ]
          },
          {
            path: 'view',
            component: ViewForm
          },
          {
            path: 'preview',
            component: PreviewForm
          },
          {
            path: 'all-responses',
            children: [
              {
                path: 'summary',
                component: AllResponsesSummary
              }
            ]
          }
        ]
      }
    ]
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'social-auth-success',
    component: SocialAuthSuccess
  },
  {
    path: 'home',
    component: Home,
    canActivate: [loggedInGuard]
  }
];
