package com.financemanager.app.presentation.transaction;

import com.financemanager.app.util.ImageStorageHelper;
import com.financemanager.app.util.ReceiptOcrHelper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AddEditTransactionDialog_MembersInjector implements MembersInjector<AddEditTransactionDialog> {
  private final Provider<ImageStorageHelper> imageStorageHelperProvider;

  private final Provider<ReceiptOcrHelper> receiptOcrHelperProvider;

  public AddEditTransactionDialog_MembersInjector(
      Provider<ImageStorageHelper> imageStorageHelperProvider,
      Provider<ReceiptOcrHelper> receiptOcrHelperProvider) {
    this.imageStorageHelperProvider = imageStorageHelperProvider;
    this.receiptOcrHelperProvider = receiptOcrHelperProvider;
  }

  public static MembersInjector<AddEditTransactionDialog> create(
      Provider<ImageStorageHelper> imageStorageHelperProvider,
      Provider<ReceiptOcrHelper> receiptOcrHelperProvider) {
    return new AddEditTransactionDialog_MembersInjector(imageStorageHelperProvider, receiptOcrHelperProvider);
  }

  @Override
  public void injectMembers(AddEditTransactionDialog instance) {
    injectImageStorageHelper(instance, imageStorageHelperProvider.get());
    injectReceiptOcrHelper(instance, receiptOcrHelperProvider.get());
  }

  @InjectedFieldSignature("com.financemanager.app.presentation.transaction.AddEditTransactionDialog.imageStorageHelper")
  public static void injectImageStorageHelper(AddEditTransactionDialog instance,
      ImageStorageHelper imageStorageHelper) {
    instance.imageStorageHelper = imageStorageHelper;
  }

  @InjectedFieldSignature("com.financemanager.app.presentation.transaction.AddEditTransactionDialog.receiptOcrHelper")
  public static void injectReceiptOcrHelper(AddEditTransactionDialog instance,
      ReceiptOcrHelper receiptOcrHelper) {
    instance.receiptOcrHelper = receiptOcrHelper;
  }
}
