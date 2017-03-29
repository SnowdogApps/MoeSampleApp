package pl.snowdog;

import org.moe.natj.c.ann.FunctionPtr;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Generated;
import org.moe.natj.general.ann.Mapped;
import org.moe.natj.general.ann.MappedReturn;
import org.moe.natj.general.ann.NInt;
import org.moe.natj.general.ann.NUInt;
import org.moe.natj.general.ann.Owned;
import org.moe.natj.general.ann.RegisterOnStartup;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.general.ptr.VoidPtr;
import org.moe.natj.objc.Class;
import org.moe.natj.objc.ObjCRuntime;
import org.moe.natj.objc.SEL;
import org.moe.natj.objc.ann.ObjCClassName;
import org.moe.natj.objc.ann.Selector;
import org.moe.natj.objc.map.ObjCObjectMapper;

import java.util.ArrayList;
import java.util.List;

import apple.NSObject;
import apple.foundation.NSArray;
import apple.foundation.NSBundle;
import apple.foundation.NSCoder;
import apple.foundation.NSIndexPath;
import apple.foundation.NSMethodSignature;
import apple.foundation.NSOperationQueue;
import apple.foundation.NSSet;
import apple.foundation.enums.NSQualityOfService;
import apple.uikit.UITableView;
import apple.uikit.UITableViewCell;
import apple.uikit.UIViewController;
import apple.uikit.protocol.UITableViewDataSource;
import apple.uikit.protocol.UITableViewDelegate;
import pl.snowdog.interfaces.IRepositoryView;
import pl.snowdog.model.Repository;
import pl.snowdog.presenter.RepositoryPresenter;
import pl.snowdog.rx.schedulers.IOSSchedulers;

@Runtime(ObjCRuntime.class)
@ObjCClassName("RepositoryListViewController")
@RegisterOnStartup
public class RepositoryListViewController extends UIViewController implements IRepositoryView, UITableViewDataSource, UITableViewDelegate {

    private static final String CELL_IDENTIFIER = "repositoryItemCell";

    static {
        NatJ.register();
    }

    private final NSOperationQueue operationQueue;
    private List<Repository> mAllRepositories = new ArrayList<>();

    protected RepositoryListViewController(Pointer peer) {
        super(peer);
        operationQueue = NSOperationQueue.alloc().init();
        operationQueue.setQualityOfService(NSQualityOfService.Background);
    }

    @Generated
    @Selector("accessInstanceVariablesDirectly")
    public static native boolean accessInstanceVariablesDirectly();

    @Generated
    @Owned
    @Selector("alloc")
    public static native RepositoryListViewController alloc();

    @Generated
    @Selector("allocWithZone:")
    @MappedReturn(ObjCObjectMapper.class)
    public static native Object allocWithZone(VoidPtr zone);

    @Generated
    @Selector("attemptRotationToDeviceOrientation")
    public static native void attemptRotationToDeviceOrientation();

    @Generated
    @Selector("automaticallyNotifiesObserversForKey:")
    public static native boolean automaticallyNotifiesObserversForKey(String key);

    @Generated
    @Selector("cancelPreviousPerformRequestsWithTarget:")
    public static native void cancelPreviousPerformRequestsWithTarget(
            @Mapped(ObjCObjectMapper.class) Object aTarget);

    @Generated
    @Selector("cancelPreviousPerformRequestsWithTarget:selector:object:")
    public static native void cancelPreviousPerformRequestsWithTargetSelectorObject(
            @Mapped(ObjCObjectMapper.class) Object aTarget, SEL aSelector,
            @Mapped(ObjCObjectMapper.class) Object anArgument);

    @Generated
    @Selector("class")
    public static native Class class_objc_static();

    @Generated
    @Selector("classFallbacksForKeyedArchiver")
    public static native NSArray<String> classFallbacksForKeyedArchiver();

    @Generated
    @Selector("classForKeyedUnarchiver")
    public static native Class classForKeyedUnarchiver();

    @Generated
    @Selector("clearTextInputContextIdentifier:")
    public static native void clearTextInputContextIdentifier(String identifier);

    @Generated
    @Selector("debugDescription")
    public static native String debugDescription_static();

    @Generated
    @Selector("description")
    public static native String description_static();

    @Generated
    @Selector("hash")
    @NUInt
    public static native long hash_static();

    @Generated
    @Selector("initialize")
    public static native void initialize();

    @Generated
    @Selector("instanceMethodForSelector:")
    @FunctionPtr(name = "call_instanceMethodForSelector_ret")
    public static native NSObject.Function_instanceMethodForSelector_ret instanceMethodForSelector(
            SEL aSelector);

    @Generated
    @Selector("instanceMethodSignatureForSelector:")
    public static native NSMethodSignature instanceMethodSignatureForSelector(
            SEL aSelector);

    @Generated
    @Selector("instancesRespondToSelector:")
    public static native boolean instancesRespondToSelector(SEL aSelector);

    @Generated
    @Selector("isSubclassOfClass:")
    public static native boolean isSubclassOfClass(Class aClass);

    @Generated
    @Selector("keyPathsForValuesAffectingValueForKey:")
    public static native NSSet<String> keyPathsForValuesAffectingValueForKey(
            String key);

    @Generated
    @Selector("load")
    public static native void load_objc_static();

    @Generated
    @Owned
    @Selector("new")
    @MappedReturn(ObjCObjectMapper.class)
    public static native Object new_objc();

    @Generated
    @Selector("resolveClassMethod:")
    public static native boolean resolveClassMethod(SEL sel);

    @Generated
    @Selector("resolveInstanceMethod:")
    public static native boolean resolveInstanceMethod(SEL sel);

    @Generated
    @Selector("setVersion:")
    public static native void setVersion(@NInt long aVersion);

    @Generated
    @Selector("superclass")
    public static native Class superclass_static();

    @Generated
    @Selector("version")
    @NInt
    public static native long version_static();

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        repositoryTableView().registerClassForCellReuseIdentifier(new org.moe.natj.objc.Class
                ("UITableViewCell"), CELL_IDENTIFIER);
        RepositoryPresenter repositoryPresenter = new RepositoryPresenter(this);
        repositoryPresenter.getRepositories(IOSSchedulers.mainThread(), IOSSchedulers.handlerThread(operationQueue));
    }

    @Override
    public UITableViewCell tableViewCellForRowAtIndexPath(UITableView tableView, NSIndexPath indexPath) {
        UITableViewCell cell = (UITableViewCell) tableView.dequeueReusableCellWithIdentifierForIndexPath(CELL_IDENTIFIER, indexPath);
        cell.textLabel().setText(mAllRepositories.get((int) indexPath.row()).getName());
        return cell;
    }

    @Override
    public long tableViewNumberOfRowsInSection(UITableView tableView, @NInt long section) {
        return mAllRepositories.size();
    }

    @Override
    public void showRepositories(List<Repository> allRepositories) {
        this.mAllRepositories = allRepositories;
        repositoryTableView().reloadData();
    }

    @Generated
    @Selector("init")
    public native RepositoryListViewController init();

    @Generated
    @Selector("initWithCoder:")
    public native RepositoryListViewController initWithCoder(NSCoder aDecoder);

    @Generated
    @Selector("initWithNibName:bundle:")
    public native RepositoryListViewController initWithNibNameBundle(
            String nibNameOrNil, NSBundle nibBundleOrNil);

    @Generated
    @Selector("repositoryTableView")
    public native UITableView repositoryTableView();

    @Generated
    @Selector("setRepositoryTableView:")
    public native void setRepositoryTableView_unsafe(UITableView value);

    @Generated
    public void setRepositoryTableView(UITableView value) {
        Object __old = repositoryTableView();
        if (value != null) {
            org.moe.natj.objc.ObjCRuntime.associateObjCObject(this, value);
        }
        setRepositoryTableView_unsafe(value);
        if (__old != null) {
            org.moe.natj.objc.ObjCRuntime.dissociateObjCObject(this, __old);
        }
    }
}