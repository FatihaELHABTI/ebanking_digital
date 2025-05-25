export interface AccountDetails {
  accountId:            string;
  balance:              number;
  currentPage:          number;
  totalPages:           number;
  pageSize:             number;
  accountOperationDTOS: AccountOperation[];
}

export interface AccountOperation {
  id:            number;
  operationDate: Date;
  amount:        number;
  description:   string;
  type:          string;
}
export interface BankAccount {
  id: string;
  type: string;
  balance: number;
  createdAt: Date;
  status: string;
}
export interface DashboardMetrics {
  totalCustomers: number;
  totalAccounts: number;
  totalBalance: number;
}
