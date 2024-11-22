package test.vtd.koreanguage.Activity;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import test.vtd.koreanguage.ViewModel.TestAddNewWordViewModel;

public class TestAddNewWordViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public TestAddNewWordViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TestAddNewWordViewModel.class)) {
            return (T) new TestAddNewWordViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}