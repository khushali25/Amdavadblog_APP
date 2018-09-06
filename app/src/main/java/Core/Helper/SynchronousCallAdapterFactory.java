package Core.Helper;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class SynchronousCallAdapterFactory extends CallAdapter.Factory {
    public static CallAdapter.Factory create() {
        return new SynchronousCallAdapterFactory();

    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (returnType.toString().contains("retrofit2.Call")) {
            return null;
        }
            return new CallAdapter<Object, Object>() {
                @Override
                public Type responseType() {
                    return returnType;
                }

                @Override
                public Object adapt(Call<Object> call) {
                    try {
                        return call.execute().body();
                    } catch (Exception e) {
                        throw new RuntimeException(e); // do something better
                    }
                }
            };
    }
}
