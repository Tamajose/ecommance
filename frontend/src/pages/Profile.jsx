import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getCurrentUser, updateMyProfile } from "../api/userApi";
import { useAuth } from "../context/AuthContext";

function formatDate(value){
    if(!value){
        return "—";
    }

    return new Date(value).toLocaleString();
}

function Field({ label, value }){
    return (
        <div className="profile-field">
            <span className="profile-label">{label}</span>
            <span className="profile-value">{value === null || value === undefined || value === "" ? "—" : value}</span>
        </div>
    );
}

const emptyForm = {
    name: "",
    phone: "",
    addressLine: "",
    addressCity: "",
    addressPostalCode: "",
    addressCountry: "",
};

export default function Profile() {
    const { user } = useAuth();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [isEditing, setIsEditing] = useState(false);
    const [form, setForm] = useState(emptyForm);
    const [saving, setSaving] = useState(false);
    const [saveError, setSaveError] = useState(null);

    async function load(){
        setLoading(true);
        setError(null);
        try{
            const data = await getCurrentUser();
            setProfile(data);
        } catch(err){
            setError(err.message || "Failed to load your details");
        } finally{
            setLoading(false);
        }
    }

    useEffect(() => {
        load();
    }, []);

    const startEditing = () => {
        setForm({
            name: profile?.name || "",
            phone: profile?.phone || "",
            addressLine: profile?.addressLine || "",
            addressCity: profile?.addressCity || "",
            addressPostalCode: profile?.addressPostalCode || "",
            addressCountry: profile?.addressCountry || "",
        });
        setSaveError(null);
        setIsEditing(true);
    };

    const handleChange = (field) => (e) => {
        setForm(prev => ({ ...prev, [field]: e.target.value }));
    };

    const handleSave = async (e) => {
        e.preventDefault();
        setSaving(true);
        setSaveError(null);
        try {
            const updated = await updateMyProfile(form);
            setProfile(updated);
            setIsEditing(false);
        } catch (err) {
            setSaveError(err.message || "Failed to update profile");
        } finally {
            setSaving(false);
        }
    };

    const displayName = profile?.name || user?.username || "My profile";
    const initials = displayName.trim().charAt(0).toUpperCase();
    const hasAddress = Boolean(
        profile?.addressLine || profile?.addressCity || profile?.addressPostalCode || profile?.addressCountry
    );
    const canSell = profile?.role === "SELLER" || profile?.role === "ADMIN";

    return (
        <div className="profile-page">
            <div className="profile-card">
                <div className="profile-header">
                    <span className="profile-avatar">{initials}</span>
                    <div className="profile-heading">
                        <h2>{displayName}</h2>
                        <p className="profile-username">@{profile?.username || user?.username}</p>
                    </div>
                    {profile?.role && <span className="profile-role">{profile.role}</span>}
                </div>

                {error && <p className="error">{error}</p>}

                {loading ? (
                    <div className="loading">Loading your details…</div>
                ) : profile && !isEditing ? (
                    <>
                        <h3 className="profile-section-title">Account</h3>
                        <div className="profile-grid">
                            <Field label="Full name" value={profile.name} />
                            <Field label="Username" value={profile.username} />
                            <Field label="Email" value={profile.email} />
                            <Field label="Phone" value={profile.phone} />
                            <Field label="Role" value={profile.role} />
                            <Field label="Status" value={profile.enabled ? "Active" : "Disabled"} />
                            <Field label="Member since" value={formatDate(profile.createdAt)} />
                        </div>

                        <h3 className="profile-section-title">Location</h3>
                        {hasAddress ? (
                            <div className="profile-grid">
                                <Field label="Address" value={profile.addressLine} />
                                <Field label="City" value={profile.addressCity} />
                                <Field label="Postal code" value={profile.addressPostalCode} />
                                <Field label="Country" value={profile.addressCountry} />
                            </div>
                        ) : (
                            <p className="profile-empty">No address saved yet.</p>
                        )}

                        <div className="profile-actions">
                            <button type="button" className="button" onClick={startEditing}>
                                Edit Profile
                            </button>
                            <Link to="/">Back to shop</Link>
                            {canSell && <Link to="/my-products">My products</Link>}
                            {profile?.role === "ADMIN" && <Link to="/admin">Admin dashboard</Link>}
                        </div>
                    </>
                ) : profile && isEditing ? (
                    <div className="checkout-form">
                        {saveError && <p className="error">{saveError}</p>}
                        <h3>Account</h3>
                        <form onSubmit={handleSave}>
                            <div className="form-group full-width">
                                <label>Full Name</label>
                                <input type="text" value={form.name} onChange={handleChange("name")} required />
                            </div>
                            <div className="form-group full-width">
                                <label>Phone</label>
                                <input type="text" value={form.phone} onChange={handleChange("phone")} />
                            </div>
                            <div className="form-group full-width">
                                <label>Street Address</label>
                                <input type="text" value={form.addressLine} onChange={handleChange("addressLine")} />
                            </div>
                            <div className="form-group">
                                <label>City</label>
                                <input type="text" value={form.addressCity} onChange={handleChange("addressCity")} />
                            </div>
                            <div className="form-group">
                                <label>Postal Code</label>
                                <input type="text" value={form.addressPostalCode} onChange={handleChange("addressPostalCode")} />
                            </div>
                            <div className="form-group">
                                <label>Country</label>
                                <input type="text" value={form.addressCountry} onChange={handleChange("addressCountry")} />
                            </div>

                            <div className="form-actions full-width profile-actions">
                                <button type="submit" className="button" disabled={saving}>
                                    {saving ? "Saving..." : "Save Changes"}
                                </button>
                                <button
                                    type="button"
                                    className="button-secondary"
                                    onClick={() => setIsEditing(false)}
                                    disabled={saving}
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                    </div>
                ) : null}
            </div>
        </div>
    );
}
